/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.smart.modules.resource.endpoint;

import com.smart.common.utils.MD5Util;
import com.smart.core.launch.constant.AppConstant;
import com.smart.core.oss.model.OssFile;
import com.smart.core.oss.model.SmartFile;
import com.smart.core.secure.annotation.PreAuth;
import com.smart.core.tenant.annotation.NonDS;
import com.smart.core.tool.api.R;
import com.smart.core.tool.constant.RoleConstant;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.FileUtil;
import com.smart.core.tool.utils.Func;
import com.smart.modules.resource.builder.oss.OssBuilder;
import com.smart.modules.resource.entity.Attach;
import com.smart.modules.resource.service.IAttachService;
import com.smart.modules.resource.vo.AttachSmartFileVo;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 对象存储端点
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
@Api(value = "对象存储端点", tags = "对象存储端点")
@RequestMapping(AppConstant.APPLICATION_RESOURCE_NAME + "/oss/endpoint")
public class OssEndpoint {

	/**
	 * 对象存储构建类
	 */
	private final OssBuilder ossBuilder;

	/**
	 * 附件表服务
	 */
	private final IAttachService attachService;

	/**
	 * 创建存储桶
	 *
	 * @param bucketName 存储桶名称
	 * @return Bucket
	 */
	@SneakyThrows
	@PostMapping("/make-bucket")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R makeBucket(@RequestParam String bucketName) {
		ossBuilder.template().makeBucket(bucketName);
		return R.success("创建成功");
	}

	/**
	 * 创建存储桶
	 *
	 * @param bucketName 存储桶名称
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/remove-bucket")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R removeBucket(@RequestParam String bucketName) {
		ossBuilder.template().removeBucket(bucketName);
		return R.success("删除成功");
	}

	/**
	 * 拷贝文件
	 *
	 * @param fileName       存储桶对象名称
	 * @param destBucketName 目标存储桶名称
	 * @param destFileName   目标存储桶对象名称
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/copy-file")
	public R copyFile(@RequestParam String fileName, @RequestParam String destBucketName, String destFileName) {
		ossBuilder.template().copyFile(fileName, destBucketName, destFileName);
		return R.success("操作成功");
	}

	/**
	 * 获取文件信息
	 *
	 * @param fileName 存储桶对象名称
	 * @return InputStream
	 */
	@SneakyThrows
	@GetMapping("/stat-file")
	public R<OssFile> statFile(@RequestParam String fileName) {
		return R.data(ossBuilder.template().statFile(fileName));
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param fileName 存储桶对象名称
	 * @return String
	 */
	@SneakyThrows
	@GetMapping("/file-path")
	public R<String> filePath(@RequestParam String fileName) {
		return R.data(ossBuilder.template().filePath(fileName));
	}


	/**
	 * 获取文件外链
	 *
	 * @param fileName 存储桶对象名称
	 * @return String
	 */
	@SneakyThrows
	@GetMapping("/file-link")
	public R<String> fileLink(@RequestParam String fileName) {
		return R.data(ossBuilder.template().fileLink(fileName));
	}

	/**
	 * 上传文件
	 *
	 * @param file 文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file")
	public R<SmartFile> putFile(@RequestParam MultipartFile file) {
		SmartFile smartFile = ossBuilder.template().putFile(file.getOriginalFilename(), file.getInputStream());
		return R.data(smartFile);
	}

	/**
	 * 上传文件
	 *
	 * @param fileName 存储桶对象名称
	 * @param file     文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file-by-name")
	public R<SmartFile> putFile(@RequestParam String fileName, @RequestParam MultipartFile file) {
		SmartFile smartFile = ossBuilder.template().putFile(fileName, file.getInputStream());
		return R.data(smartFile);
	}

	/**
	 * 上传文件并保存至附件表
	 *
	 * @param file 文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file-attach")
	public R<AttachSmartFileVo> putFileAttach(@RequestParam MultipartFile file) {
		String fileName = file.getOriginalFilename();
		SmartFile smartFile = ossBuilder.template().putFile(fileName, file.getInputStream());

		AttachSmartFileVo attachSmartFileVo = new AttachSmartFileVo();
		BeanUtil.copyProperties(smartFile, attachSmartFileVo);
		File fileMd5 = new File(fileName);
		FileCopyUtils.copy(file.getBytes(), fileMd5);
		String fileMD5String = MD5Util.getFileMD5String(fileMd5);
		Long attachId = buildAttach(fileName, file.getSize(), smartFile);
		attachSmartFileVo.setAttachId(attachId);
		attachSmartFileVo.setSize(file.getSize());
		attachSmartFileVo.setMd5(fileMD5String);
		FileUtil.deleteQuietly(fileMd5);

		return R.data(attachSmartFileVo);
	}

	/**
	 * 上传文件并保存至附件表
	 *
	 * @param fileName 存储桶对象名称
	 * @param file     文件
	 * @return ObjectStat
	 */
	@SneakyThrows
	@PostMapping("/put-file-attach-by-name")
	public R<SmartFile> putFileAttach(@RequestParam String fileName, @RequestParam MultipartFile file) {
		SmartFile smartFile = ossBuilder.template().putFile(fileName, file.getInputStream());
		Long attachId = buildAttach(fileName, file.getSize(), smartFile);
		smartFile.setAttachId(attachId);
		return R.data(smartFile);
	}

	/**
	 * 构建附件表
	 *
	 * @param fileName  文件名
	 * @param fileSize  文件大小
	 * @param smartFile 对象存储文件
	 * @return attachId
	 */
	private Long buildAttach(String fileName, Long fileSize, SmartFile smartFile) {
		String fileExtension = FileUtil.getFileExtension(fileName);
		Attach attach = new Attach();
		attach.setDomain(smartFile.getDomain());
		attach.setLink(smartFile.getLink());
		attach.setName(smartFile.getName());
		attach.setOriginalName(smartFile.getOriginalName());
		attach.setAttachSize(fileSize);
		attach.setExtension(fileExtension);
		attachService.save(attach);
		return attach.getId();
	}

	/**
	 * 删除文件
	 *
	 * @param fileName 存储桶对象名称
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/remove-file")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R removeFile(@RequestParam String fileName) {
		ossBuilder.template().removeFile(fileName);
		return R.success("操作成功");
	}

	/**
	 * 批量删除文件
	 *
	 * @param fileNames 存储桶对象名称集合
	 * @return R
	 */
	@SneakyThrows
	@PostMapping("/remove-files")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R removeFiles(@RequestParam String fileNames) {
		ossBuilder.template().removeFiles(Func.toStrList(fileNames));
		return R.success("操作成功");
	}

}
