package com.example.latte.eye.mian.TestEyes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.latte.eye.R;
import com.example.latte_core.activities.ProxyActivity;
import com.example.latte_core.util.Recognize.RecognizeCommands;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TestEyeActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = TestEyeActivity.class.getSimpleName();
    static final int DIRECT_UNCLEAR = -1;
    static final int DIRECT_UP = 0;
    static final int DIRECT_RIGHT = 1;
    static final int DIRECT_DOWN = 2;
    static final int DIRECT_LEFT = 3;
    static final String TEXT_UP = "上";
    static final String TEXT_RIGHT = "右";
    static final String TEXT_DOWN = "下";
    static final String TEXT_LEFT = "左";
    static final String TEXT_UNCLEAR = "看不清";
    static final String TEXT_NOT_SEE = "看不见";
    static final String TEXT_NOT_VISIBLE = "不清楚";
    private static final String GRAMMAR_TYPE_ABNF = "abnf";
    private static final int SAMPLE_RATE = 16000;
    private static final int SAMPLE_DURATION_MS = 1000;
    private static final int RECORDING_LENGTH = (int) (SAMPLE_RATE * SAMPLE_DURATION_MS / 1000);
    private static final long AVERAGE_WINDOW_DURATION_MS = 1000;
    private static final float DETECTION_THRESHOLD = 0.50f;
    private static final int SUPPRESSION_MS = 1500;
    private static final int MINIMUM_COUNT = 3;
    private static final long MINIMUM_TIME_BETWEEN_SAMPLES_MS = 30;
    private static final String LABEL_FILENAME = "file:///android_asset/conv_actions_labels.txt";
    private static final String MODEL_FILENAME = "file:///android_asset/conv_actions_frozen.tflite";
    short[] recordingBuffer = new short[RECORDING_LENGTH];
    int recordingOffset = 0;
    boolean shouldContinue = true;
    private Thread recordingThread;
    boolean shouldContinueRecognition = true;
    private Thread recognitionThread;
    private final ReentrantLock recordingBufferLock = new ReentrantLock();
    private Handler handler = new Handler();
    private List<String> labels = new ArrayList<String>();
    private List<String> displayedLabels = new ArrayList<>();
    private RecognizeCommands recognizeCommands = null;
    // UI elements.
    private static final int REQUEST_RECORD_AUDIO = 13;
    private static final String LOG_TAG = SpeechActivity.class.getSimpleName();
    private Interpreter tfLite;
    private ImageView mImgEye;
    private int mIndex = 0;
    private float mLastToDegrees = 0;
    private int mDirect = 1;
    private int mRet = 0;
    private ArrayList<Item> mUserList = new ArrayList<>();
    private Toast mToast;


    // 语音识别对象
    // private SpeechRecognizer mAsr;
    // 缓存
    private SharedPreferences mSharedPreferences;
    private static final String KEY_GRAMMAR_ABNF_ID = "grammar_abnf_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_eye);
        init();
        String actualLabelFilename = LABEL_FILENAME.split("file:///android_asset/", -1)[1];
        Log.i(LOG_TAG, "Reading labels from: " + actualLabelFilename);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(actualLabelFilename)));
            String line;
            while ((line = br.readLine()) != null) {
                labels.add(line);
                if (line.charAt(0) != '_') {
                    displayedLabels.add(line.substring(0, 1).toUpperCase() + line.substring(1));
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException("Problem reading label file!", e);
        }

        // Set up an object to smooth recognition results to increase accuracy.
        recognizeCommands =
                new RecognizeCommands(
                        labels,
                        AVERAGE_WINDOW_DURATION_MS,
                        DETECTION_THRESHOLD,
                        SUPPRESSION_MS,
                        MINIMUM_COUNT,
                        MINIMUM_TIME_BETWEEN_SAMPLES_MS);

        String actualModelFilename = MODEL_FILENAME.split("file:///android_asset/", -1)[1];
        try {
            tfLite = new Interpreter(loadModelFile(getAssets(), actualModelFilename));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        tfLite.resizeInput(0, new int[] {RECORDING_LENGTH, 1});
        tfLite.resizeInput(1, new int[] {1});

        // Start the recording and recognition threads.
        requestMicrophonePermission();
        startRecording();
        startRecognition();
    }

    /**
     * 初始化view
     */
    private void init() {
        mImgEye = (ImageView) findViewById(R.id.img_e);
        findViewById(R.id.rl_top).setOnClickListener(this);
        findViewById(R.id.rl_bottom).setOnClickListener(this);
        findViewById(R.id.rl_left).setOnClickListener(this);
        findViewById(R.id.rl_right).setOnClickListener(this);
    }


    /**
     * 初始化讯飞在线语音识别
     */
//    private void initAsr() {
//        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
//        mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
//        // 初始化识别对象
//        //mAsr = SpeechRecognizer.createRecognizer(this, mInitListener);
//
//    }

    /** Memory-map the model file in Assets. */
    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
            throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_top) {
            up();
        } else if (id == R.id.rl_bottom) {
            down();
        } else if (id == R.id.rl_left) {
            left();
        } else if (id == R.id.rl_right) {
            right();
        }
    }


    private void up() {

        final boolean flag = mDirect == DIRECT_UP;

        //已经错误一次 且本次也错误
        if (!checkErrorTimes() && !flag) {
            showResult();
            return;
        }
        mUserList.add(new Item(mIndex, mDirect, mDirect == DIRECT_UP ? DIRECT_UP : DIRECT_UNCLEAR));
        next(flag);
    }

    private void down() {
        final boolean flag = mDirect == DIRECT_DOWN;

        //已经错误一次 且本次也错误
        if (!checkErrorTimes() && !flag) {
            showResult();
            return;
        }
        mUserList.add(new Item(mIndex, mDirect, flag? DIRECT_DOWN : DIRECT_UNCLEAR));
        next(flag);

    }

    private void left() {
        final boolean flag = mDirect == DIRECT_LEFT;

        //已经错误一次 且本次也错误
        if (!checkErrorTimes() && !flag) {
            showResult();
            return;
        }
        mUserList.add(new Item(mIndex, mDirect, flag ? DIRECT_LEFT : DIRECT_UNCLEAR));
        next(flag);

    }

    private void right() {
        final boolean flag = mDirect == DIRECT_RIGHT;
        //已经错误一次 且本次也错误
        if (!checkErrorTimes() && !flag) {
            showResult();
            return;
        }

        mUserList.add(new Item(mIndex, mDirect, flag ? DIRECT_RIGHT : DIRECT_UNCLEAR));
        next(flag);

    }

    /**
     * 看不清楚 两次后终止
     */
    private void unClear() {
        //已经错误一次 再次错误
        if (!checkErrorTimes()) {
            showResult();
            return;
        }
        mUserList.add(new Item(mIndex, mDirect, DIRECT_UNCLEAR));
        next(false);
    }


    /**
     * 检查是否已经错误过一次
     *
     * @return flag
     */
    private boolean checkErrorTimes() {
        boolean flag = true;
        for (Item item : mUserList) {
            int index = item.getIndex();
            if (index == mIndex && item.getUser_direct() == DIRECT_UNCLEAR) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    private void next(boolean clear) {
        if (clear) {
            mIndex++;
        }
        if (mIndex > 8) {
            showResult();
        } else {
            playAnim();
        }
    }


    private void showResult() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("测试结果");
        String msg = "";
        for (Item item : mUserList) {
            msg += item.getResult();
        }
        dialog.setMessage(msg);
        dialog.create();
        dialog.setNegativeButton("重新测试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(TestEyeActivity.this, TestEyeActivity.class));
            }
        });
        dialog.setPositiveButton("退出测试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }


    /**
     * 播放动画 缩放 + 旋转
     * 随着mIndex++ 图片每次缩小10%
     * mDirect方向随机生成 如果错误一次不缩小 只换旋转
     */
    private void playAnim() {

        float from = 1 - (float) (0.1 * mIndex);
        float to = 1 - (float) (0.1 * (mIndex + 1));

        AnimationSet set = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(scaleAnimation);

        Random random = new Random();
        int rd = random.nextInt(4);

        //保存本次旋转角度 以便下次使用
        float fromDegrees = mLastToDegrees;
        mLastToDegrees = fromDegrees + 90 * rd;
        float toDegrees = mLastToDegrees;


        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        set.addAnimation(rotate);

        set.setDuration(500);
        set.setFillAfter(true);
        mImgEye.startAnimation(set);


        //记录旋转后的方向
        mDirect += rd;
        if (mDirect < 4) {
            mDirect += 4;
        }
        mDirect %= 4;

        //mAsr.startListening(mRecognizerListener);

    }




    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }


    /**
     * 内部类 记录每次选择
     */
    class Item {
        //次数
        private int index;
        //系统的方向
        private int direct;
        //用户选择的方向
        private int user_direct;

        public Item(int index, int direct, int user_direct) {
            this.index = index;
            this.direct = direct;
            this.user_direct = user_direct;
        }

        public int getIndex() {
            return index;
        }

        public int getUser_direct() {
            return user_direct;
        }

        public String getResult() {
            StringBuilder sb = new StringBuilder();
            sb.append("第");
            sb.append(index + 1);
            sb.append("次");
            sb.append("  ");
            sb.append(appendText(direct));
            sb.append("  ");
            sb.append("选择");
            sb.append("  :  ");
            sb.append(appendText(user_direct));
            sb.append("  ---->  ");
            sb.append(direct == user_direct ? "正确" : "错误");
            sb.append("\n");
            return sb.toString();
        }

        private String appendText(int direct) {
            String text = "";
            switch (direct) {
                case DIRECT_UP:
                    text = "上";
                    break;
                case DIRECT_RIGHT:
                    text = "右";
                    break;
                case DIRECT_DOWN:
                    text = "下";
                    break;
                case DIRECT_LEFT:
                    text = "左";
                    break;
                case DIRECT_UNCLEAR:
                    text = "看不清";
                    break;
            }

            return text;
        }


    }

    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[] {android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startRecording();
            startRecognition();
        }
    }

    public synchronized void startRecording() {
        if (recordingThread != null) {
            return;
        }
        shouldContinue = true;
        recordingThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                record();
                            }
                        });
        recordingThread.start();
    }

    public synchronized void stopRecording() {
        if (recordingThread == null) {
            return;
        }
        shouldContinue = false;
        recordingThread = null;
    }

    private void record() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        // Estimate the buffer size we'll need for this device.
        int bufferSize =
                AudioRecord.getMinBufferSize(
                        SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLE_RATE * 2;
        }
        short[] audioBuffer = new short[bufferSize / 2];

        AudioRecord record =
                new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(LOG_TAG, "Audio Record can't initialize!");
            return;
        }

        record.startRecording();

        Log.v(LOG_TAG, "Start recording");

        // Loop, gathering audio data and copying it to a round-robin buffer.
        while (shouldContinue) {
            int numberRead = record.read(audioBuffer, 0, audioBuffer.length);
            int maxLength = recordingBuffer.length;
            int newRecordingOffset = recordingOffset + numberRead;
            int secondCopyLength = Math.max(0, newRecordingOffset - maxLength);
            int firstCopyLength = numberRead - secondCopyLength;
            // We store off all the data for the recognition thread to access. The ML
            // thread will copy out of this buffer into its own, while holding the
            // lock, so this should be thread safe.
            recordingBufferLock.lock();
            try {
                System.arraycopy(audioBuffer, 0, recordingBuffer, recordingOffset, firstCopyLength);
                System.arraycopy(audioBuffer, firstCopyLength, recordingBuffer, 0, secondCopyLength);
                recordingOffset = newRecordingOffset % maxLength;
            } finally {
                recordingBufferLock.unlock();
            }
        }

        record.stop();
        record.release();
    }

    public synchronized void startRecognition() {
        if (recognitionThread != null) {
            return;
        }
        shouldContinueRecognition = true;
        recognitionThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                recognize();
                            }
                        });
        recognitionThread.start();
    }

    public synchronized void stopRecognition() {
        if (recognitionThread == null) {
            return;
        }
        shouldContinueRecognition = false;
        recognitionThread = null;
    }

    private void recognize() {

        Log.v(LOG_TAG, "Start recognition");

        short[] inputBuffer = new short[RECORDING_LENGTH];
        float[][] floatInputBuffer = new float[RECORDING_LENGTH][1];
        float[][] outputScores = new float[1][labels.size()];
        int[] sampleRateList = new int[] {SAMPLE_RATE};

        // Loop, grabbing recorded data and running the recognition model on it.
        while (shouldContinueRecognition) {
            long startTime = new Date().getTime();
            // The recording thread places data in this round-robin buffer, so lock to
            // make sure there's no writing happening and then copy it to our own
            // local version.
            recordingBufferLock.lock();
            try {
                int maxLength = recordingBuffer.length;
                int firstCopyLength = maxLength - recordingOffset;
                int secondCopyLength = recordingOffset;
                System.arraycopy(recordingBuffer, recordingOffset, inputBuffer, 0, firstCopyLength);
                System.arraycopy(recordingBuffer, 0, inputBuffer, firstCopyLength, secondCopyLength);
            } finally {
                recordingBufferLock.unlock();
            }

            // We need to feed in float values between -1.0f and 1.0f, so divide the
            // signed 16-bit inputs.
            for (int i = 0; i < RECORDING_LENGTH; ++i) {
                floatInputBuffer[i][0] = inputBuffer[i] / 32767.0f;
            }

            Object[] inputArray = {floatInputBuffer, sampleRateList};
            Map<Integer, Object> outputMap = new HashMap<>();
            outputMap.put(0, outputScores);

            // Run the model.
            tfLite.runForMultipleInputsOutputs(inputArray, outputMap);

            // Use the smoother to figure out if we've had a real recognition event.
            long currentTime = System.currentTimeMillis();
            final RecognizeCommands.RecognitionResult result =
                    recognizeCommands.processLatestResults(outputScores[0], currentTime);
//            lastProcessingTimeMs = new Date().getTime() - startTime;
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {

//                            inferenceTimeTextView.setText(lastProcessingTimeMs + " ms");

                            // If we do have a new command, highlight the right list entry.
                            if (!result.foundCommand.startsWith("_") && result.isNewCommand) {
                                int labelIndex = -1;
                                for (int i = 0; i < labels.size(); ++i) {
                                    if (labels.get(i).equals(result.foundCommand)) {
                                        labelIndex = i;
                                    }
                                }

                                switch (labelIndex - 2) {
                                    case 0:
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        up();
                                        break;
                                    case 3:
                                        down();
                                        break;
                                    case 4:
                                        left();
                                        break;
                                    case 5:
                                        right();
                                        break;
                                    case 6:
                                        break;
                                    case 7:
                                        break;
                                    case 8:
                                        break;
                                    case 9:
                                        break;
                                }

//                                if (selectedTextView != null) {
//                                    selectedTextView.setBackgroundResource(R.drawable.round_corner_text_bg_selected);
//                                    final String score = Math.round(result.score * 100) + "%";
//                                    selectedTextView.setText(selectedTextView.getText() + "\n" + score);
//                                    selectedTextView.setTextColor(
//                                            getResources().getColor(android.R.color.holo_orange_light));
//                                    handler.postDelayed(
//                                            new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    String origionalString =
//                                                            selectedTextView.getText().toString().replace(score, "").trim();
//                                                    selectedTextView.setText(origionalString);
//                                                    selectedTextView.setBackgroundResource(
//                                                            R.drawable.round_corner_text_bg_unselected);
//                                                    selectedTextView.setTextColor(
//                                                            getResources().getColor(android.R.color.darker_gray));
//                                                }
//                                            },
//                                            750);
//                                }
                            }
                        }
                    });
            try {
                // We don't need to run too frequently, so snooze for a bit.
                Thread.sleep(MINIMUM_TIME_BETWEEN_SAMPLES_MS);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        Log.v(LOG_TAG, "End recognition");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
//        mAsr.cancel();
//        mAsr.destroy();
    }
}
