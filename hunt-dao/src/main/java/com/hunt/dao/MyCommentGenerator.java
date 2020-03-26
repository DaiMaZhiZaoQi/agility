package com.hunt.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 * 生成实体类注释
 * @author williambai
 *
 */
public class MyCommentGenerator  extends DefaultCommentGenerator implements CommentGenerator{
	private Properties properties;
	private Properties systemPro;
	private boolean suppressDate;
	private boolean suppressAllComments;
	private String currentDateStr;
	public MyCommentGenerator() {
		super();
		properties = new Properties();
		systemPro = System.getProperties();
		suppressDate = false;
		suppressAllComments = false;
		currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
	}
	@Override
	public void addFieldComment(Field field,IntrospectedTable introspectedTable,IntrospectedColumn introspectedColumn) {
			// 添加字段注释
		StringBuffer sb = new StringBuffer();
		field.addJavaDocLine("/**");
		if (introspectedColumn.getRemarks() != null) {
			field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
			sb.append(" * 表字段 : ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			sb.append('.');
			sb.append(introspectedColumn.getActualColumnName());
			field.addJavaDocLine(sb.toString());
			field.addJavaDocLine(" * ");
			field.addJavaDocLine(" */");
		}
	}

		public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
			if (suppressAllComments) {
				return;
			}
			StringBuilder sb = new StringBuilder();
			innerClass.addJavaDocLine("/**");
			sb.append(" * ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			sb.append(" ");
			sb.append(getDateString());
			innerClass.addJavaDocLine(sb.toString());
			innerClass.addJavaDocLine(" */");
		}
	
		public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
			if (suppressAllComments) {
				return;
			}
			StringBuilder sb = new StringBuilder();
			innerEnum.addJavaDocLine("/**");
			sb.append(" * ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			innerEnum.addJavaDocLine(sb.toString());
			innerEnum.addJavaDocLine(" */");
		}
		public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
			if (suppressAllComments) {
				return;
			}
			StringBuilder sb = new StringBuilder();
			field.addJavaDocLine("/**");
			sb.append(" * ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			field.addJavaDocLine(sb.toString());
			field.addJavaDocLine(" */");
		}
		public void addGetterComment(Method method, IntrospectedTable introspectedTable,
				IntrospectedColumn introspectedColumn) {
			if (suppressAllComments) {
				return;
			}
			method.addJavaDocLine("/**");
			StringBuilder sb = new StringBuilder();
			sb.append(" * 获取");
			sb.append(introspectedColumn.getRemarks());
			method.addJavaDocLine(sb.toString());
			sb.setLength(0);
			sb.append(" * @return ");
			sb.append(introspectedColumn.getActualColumnName());
			sb.append(" ");
			sb.append(introspectedColumn.getRemarks());
			method.addJavaDocLine(sb.toString());
			//      addJavadocTag(method, false);
			method.addJavaDocLine(" */");
		}
		public void addSetterComment(Method method, IntrospectedTable introspectedTable,
				IntrospectedColumn introspectedColumn) {
			if (suppressAllComments) {
				return;
			}
			method.addJavaDocLine("/**");
			StringBuilder sb = new StringBuilder();
			sb.append(" * 设置");
			sb.append(introspectedColumn.getRemarks());
			method.addJavaDocLine(sb.toString());
			Parameter parm = method.getParameters().get(0);
			sb.setLength(0);
			sb.append(" * @param ");
			sb.append(parm.getName());
			sb.append(" ");
			sb.append(introspectedColumn.getRemarks());
			method.addJavaDocLine(sb.toString());
			//      addJavadocTag(method, false);
			method.addJavaDocLine(" */");
		}
		public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
			if (suppressAllComments) {
				return;
			}
			
			StringBuilder sb = new StringBuilder();
			
			innerClass.addJavaDocLine("/**");
			sb.append(" * ");
			sb.append(introspectedTable.getFullyQualifiedTable());
			innerClass.addJavaDocLine(sb.toString());
			sb.setLength(0);
			sb.append(" * @author ");
			sb.append(systemPro.getProperty("user.name"));
			sb.append(" ");
			sb.append(currentDateStr);
			innerClass.addJavaDocLine(" */");
		}
}
