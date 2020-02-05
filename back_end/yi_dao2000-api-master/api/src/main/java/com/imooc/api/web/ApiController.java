package com.imooc.api.web;

import com.imooc.api.utils.MyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * api
 *
 * @author 尹冬飞
 * @create 2017-10-12 10:45
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	@RequestMapping("about")
	public String about() {
		return MyUtils.getJsonContentInStatic("about");
	}

	@RequestMapping("add_shop_cart")
	public String add_shop_cart(@RequestParam int count) {
		return MyUtils.getJsonContentInStatic("add_shop_cart");
	}

	@RequestMapping("address")
	public String address() {
		return MyUtils.getJsonContentInStatic("address");
	}

	@RequestMapping("classify_content")
	public String classify_content() {
		return MyUtils.getJsonContentInStatic("classify_content");
	}

	@RequestMapping("goods_detail")
	public String goods_detail_data_1(@RequestParam int goods_id) {
		return MyUtils.getJsonContentInStatic("goods_detail_data_" + goods_id);
	}

	@RequestMapping("index_2_data")
	public String index_2_data() {
		return MyUtils.getJsonContentInStatic("index_2_data");
	}

	@RequestMapping("index_data")
	public String index_data() {
		return MyUtils.getJsonContentInStatic("index_data");
	}

	@RequestMapping("info")
	public String info() {
		return MyUtils.getJsonContentInStatic("info");
	}

	@RequestMapping("order_list")
	public String order_list() {
		return MyUtils.getJsonContentInStatic("order_list");
	}

	@RequestMapping("shop_cart_data")
	public String shop_cart_data() {
		return MyUtils.getJsonContentInStatic("shop_cart_data");
	}

	@RequestMapping("sort_content_data_1")
	public String sort_content_data_1() {
		return MyUtils.getJsonContentInStatic("sort_content_data_1");
	}

	@RequestMapping("sort_content_data_2")
	public String sort_content_data_2() {
		return MyUtils.getJsonContentInStatic("sort_content_data_2");
	}

	@RequestMapping("sort_list_data")
	public String sort_list_data() {
		return MyUtils.getJsonContentInStatic("sort_list_data");
	}

	@RequestMapping("/user_profile")
	public String user_profile(@RequestParam String email) {
		return MyUtils.getJsonContentInStatic("user_profile");
	}

	@RequestMapping("goods1")
	public String goods1() {
		return MyUtils.getJsonContentInStatic("goods1");
	}

	@RequestMapping("goods2")
	public String goods2() {
		return MyUtils.getJsonContentInStatic("goods2");
	}

	@RequestMapping("goods3")
	public String goods3() {
		return MyUtils.getJsonContentInStatic("about2");
	}

	@RequestMapping("upload_file")
	@ResponseBody
	public String upload_file(@RequestParam("file") MultipartFile file) {
		System.out.println(file.getName());
		return file.getName();
	}
}
