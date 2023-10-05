package com.smart.common.utils;

import com.smart.common.utils.inter.TreeChildren;
import com.smart.common.utils.inter.TreeKey;
import com.smart.common.utils.inter.TreeParentKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 递归求树形工具类
 *
 * @since 2023/3/8
 */
public class TreeUtils {

	/**
	 * 集合转化为树形
	 *
	 * @param list             集合
	 * @param highestParentKey 最高层父节点值
	 * @param <T>              泛型
	 * @return 树形
	 */
	public static <T> List<T> getTree(List<T> list, Object highestParentKey) {
		if (Objects.isNull(list) || list.isEmpty()) {
			return Collections.emptyList();
		}
		Field key = null;
		Field parentKey = null;
		Field children = null;
		Field[] fields = list.get(0).getClass().getDeclaredFields();
		for (Field field : fields) {
			if (Objects.isNull(key)) {
				TreeKey treeKey = field.getAnnotation(TreeKey.class);
				if (Objects.nonNull(treeKey)) {
					key = field;
					continue;
				}
			}
			if (Objects.isNull(parentKey)) {
				TreeParentKey treeParentKey = field.getAnnotation(TreeParentKey.class);
				if (Objects.nonNull(treeParentKey)) {
					parentKey = field;
					continue;
				}
			}
			if (Objects.isNull(children)) {
				TreeChildren treeChildren = field.getAnnotation(TreeChildren.class);
				if (Objects.nonNull(treeChildren)) {
					children = field;
					continue;
				}
			}
		}
		if (Objects.isNull(key) || Objects.isNull(parentKey) || Objects.isNull(children)) {
			return Collections.emptyList();
		}
		key.setAccessible(true);
		parentKey.setAccessible(true);
		children.setAccessible(true);
		// 获取最高层数据
		List<T> highs = new ArrayList<>();
		try {
			for (T t : list) {
				Object pk = parentKey.get(t);
				if (getString(pk).equals(getString(highestParentKey))) {
					highs.add(t);
				}
			}
			// 获取最高层子孙节点
			for (T t : highs) {
				setChildren(list, t, key, parentKey, children);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return highs;
	}

	/**
	 * 获取子孙节点
	 *
	 * @param list      集合
	 * @param parent    父节点对象
	 * @param key       唯一属性
	 * @param parentKey 父唯一属性
	 * @param children  节点
	 * @param <T>       泛型
	 * @return 带有子孙集合的父节点对象
	 * @throws IllegalAccessException
	 */
	private static <T> T setChildren(List<T> list, T parent, Field key, Field parentKey, Field children) throws IllegalAccessException {
		Object k = key.get(parent);
		List<T> tempList = new ArrayList<>();
		for (T t : list) {

			Object pk = parentKey.get(t);
			if (getString(k).equals(getString(pk))) {

				tempList.add(setChildren(list, t, key, parentKey, children));
			} else {
				if (!Objects.isNull(children.get(t)) && ((List<T>) children.get(t)).size() > 0) {
					tempList = (List<T>) children.get(t);


				}
			}
		}
		children.set(parent, tempList);
		return parent;
	}

	/**
	 * 获取字符串
	 *
	 * @param o 值
	 * @return 字符串
	 */
	private static String getString(Object o) {
		return Objects.isNull(o) ? "" : o.toString();
	}

}
