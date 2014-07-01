/**
 * Copyright 1996-2013 Founder International Co.,Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author yangguangftlp
 */
package org.foxbpm.engine.impl.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.foxbpm.engine.exception.FoxBPMException;

/**
 * <p>
 * 邮件发送类
 * </p>
 * <p>
 * 支持普通模式和HTML模式，可发送多个附件，支持SMTP服务器认证。<br>
 * 基于javamail开发，使用时请将javamail包含在classpath系统变量中。
 * </p>
 * <p>
 * <br>
 * 使用说明：
 * </p>
 * <p>
 * Mail mail=new Mail();
 * </p>
 * <p>
 * mail.setXXX ....
 * </p>
 * <p>
 * mail.send();<br>
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MailUtil {

	private final static Logger logger = Logger.getLogger(MailUtil.class);

	/**
	 * 邮件地址标示
	 */
	public static final String MAIL_MARK = "@";
	/**
	 * 普通模式
	 */
	public static final String MODE_TEXT = "text/plain;charset=gb2312";

	/**
	 * HTML模式
	 */
	public static final String MODE_HTML = "text/html;charset=gb2312";

	private Address[] to = null;
	private Address[] cc = null;
	private Address[] bcc = null;
	private String from = "";
	private String title = "";
	private String content = "";
	private String smtpHost = "";
	private int smtpPort = 25;

	private String content_type = MODE_TEXT;
	private String htmlMailDesc = "";

	private String smtpUser = "";
	private String smtpPassword = "";
	private boolean isAuthenticationSMTP = false;

	private Vector vFiles = new Vector();
	private Vector vURLs = new Vector();

	public MailUtil() {
	}

	/**
	 * 设置SMTP服务器，使用默认端口
	 * 
	 * @param server
	 *            SMTP服务器IP
	 */
	public void setSmtpHost(String server) {
		this.smtpHost = server;
	}

	/**
	 * 设置SMTP服务器
	 * 
	 * @param server
	 *            SMTP服务器IP
	 * @param port
	 *            端口
	 */
	public void setSmtpHost(String server, int port) {
		this.smtpHost = server;
		this.smtpPort = port;
	}

	/**
	 * 设置收件人地址
	 * 
	 * @param aEmail
	 *            收件人Email地址
	 */
	public void setTo(String aEmail) {
		String[] s = new String[1];
		s[0] = aEmail;
		this.to = getAddress(s);
	}

	/**
	 * 设置多个收件人地址
	 * 
	 * @param Emails
	 *            收件人Email地址
	 */
	public void setTo(String[] Emails) {
		this.to = getAddress(Emails);
	}

	/**
	 * 设置抄送地址
	 * 
	 * @param aEmail
	 *            抄送地址
	 */
	public void setCC(String aEmail) {
		String[] s = new String[1];
		s[0] = aEmail;
		this.cc = getAddress(s);
	}

	/**
	 * 设置多个抄送地址
	 * 
	 * @param Emails
	 *            抄送地址
	 */
	public void setCC(String[] Emails) {
		this.cc = getAddress(Emails);
	}

	/**
	 * 设置暗送地址
	 * 
	 * @param Emails
	 *            暗送地址
	 */

	public void setBCC(String aEmail) {
		String[] s = new String[1];
		s[0] = aEmail;
		this.bcc = getAddress(s);
	}

	/**
	 * 设置多个暗送地址
	 * 
	 * @param Emails
	 *            暗送地址
	 */
	public void setBCC(String[] Emails) {
		this.bcc = getAddress(Emails);
	}

	/**
	 * 设置发件人地址
	 * 
	 * @param aEmail
	 *            发件人地址
	 */
	public void setFrom(String aEmail) {
		this.from = aEmail;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param mailTitle
	 *            邮件主题
	 */
	public void setSubject(String mailTitle) {
		this.title = mailTitle;
	}

	/**
	 * 设置邮件文字内容
	 * 
	 * @param mailContent
	 *            邮件文字内容
	 */
	public void setBody(String mailContent) {
		this.content = mailContent;
	}

	/**
	 * 设置邮件字符类型
	 * 
	 * @param contentType
	 *            请从静态变量TEXT和HTML中选择
	 */
	public void setContentType(String contentType) {
		this.content_type = contentType;
	}

	/**
	 * 设置HTML格式邮件在一般模式下显示的说明
	 * 
	 * @param desc
	 *            说明文字
	 */
	public void setHtmlMailDesc(String desc) {
		this.htmlMailDesc = desc;
	}

	/**
	 * 设置SMTP服务器用户认证
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 */
	public void setSmtpAuthentication(String username, String password) {
		this.smtpUser = username;
		this.smtpPassword = password;
		this.isAuthenticationSMTP = true;
	}

	/**
	 * 添加附件
	 * 
	 * @param afile
	 *            本地文件
	 */
	public void addAttachment(File afile) {
		vFiles.add(afile);
	}

	/**
	 * 添加附件
	 * 
	 * @param fileURL
	 *            文件URL
	 */
	public void addAttachment(URL fileURL) {
		vURLs.add(fileURL);
	}

	/**
	 * 标示邮件是否附带附件
	 * 
	 * @return
	 */
	public boolean hasAttachment() {
		return vFiles.size() + vURLs.size() > 0;
	}

	/**
	 * 发送邮件
	 */
	public void send() {
		logger.debug("start send()");
		try {
			Properties server = new Properties();
			if (StringUtil.isEmpty(this.smtpHost)) {
				logger.error("The smtpHost is empty,Please set SMTP host");
				throw new NullPointerException("Please set SMTP host");
			} else {
				server.put("mail.smtp.host", this.smtpHost);
			}
			server.put("mail.smtp.port", String.valueOf(this.smtpPort));
			if (this.isAuthenticationSMTP) {
				server.put("mail.smtp.auth", "true");
			}
			Session conn = Session.getInstance(server, null);

			MimeMessage msg = new MimeMessage(conn);
			if (StringUtil.isEmpty(this.from)) {
				logger.error("The sender is empty,Please set FROM address");
				throw new NullPointerException("Please set FROM address");
			} else {
				msg.setFrom(new InternetAddress(this.from));
			}
			if (this.to != null) {
				msg.setRecipients(Message.RecipientType.TO, this.to);
			}
			if (this.cc != null) {
				msg.setRecipients(Message.RecipientType.CC, this.cc);
			}
			if (this.bcc != null) {
				msg.setRecipients(Message.RecipientType.BCC, this.bcc);
			}
			try {
				msg.setSubject(MimeUtility.encodeText(this.title));
			} catch (UnsupportedEncodingException e) {
				logger.error("The mail header conversion failed", e);
				e.printStackTrace();
				throw new FoxBPMException("The mail header conversion failed", e);
			}
			if (!hasAttachment()) {
				// 如果没有带附件
				if (this.isHtmlModeMail()) {
					// 是HTML格式的邮件
					if (!this.hasHtmlDesc()) {
						msg.setContent(this.content, this.content_type);
					} else {
						Multipart mp = new MimeMultipart();
						MimeBodyPart mbp = null;
						mbp = new MimeBodyPart();
						mbp.setContent(this.content, this.content_type);
						mp.addBodyPart(mbp);
						mbp = new MimeBodyPart();
						mbp.setContent(this.htmlMailDesc, MODE_TEXT);
						mp.addBodyPart(mbp);
						msg.setContent(mp);
					}
				} else {
					// 是文本格式的邮件
					msg.setText(this.content);
				}
			} else {
				// 有附件
				Multipart mp = new MimeMultipart();
				MimeBodyPart mbp = null;
				// 邮件正文
				for (int i = 0; i < vFiles.size(); i++) {
					mbp = new MimeBodyPart();
					File file = (File) vFiles.get(i);
					FileDataSource fds = new FileDataSource(file);
					mbp.setDataHandler(new DataHandler(fds));
					mbp.setFileName(file.getName());
					mp.addBodyPart(mbp);
				}
				for (int i = 0; i < vURLs.size(); i++) {
					mbp = new MimeBodyPart();
					URL url = (URL) vURLs.get(i);
					mbp.setDataHandler(new DataHandler(url));
					mbp.setFileName(url.getFile());
					mp.addBodyPart(mbp);
				}
				mbp = new MimeBodyPart();
				mbp.setContent(this.content, this.content_type);
				mp.addBodyPart(mbp);
				if (this.isHtmlModeMail() && this.hasHtmlDesc()) {
					mbp = new MimeBodyPart();
					mbp.setContent(this.htmlMailDesc, MODE_TEXT);
					mp.addBodyPart(mbp);
				}
				msg.setContent(mp);
			}
			msg.saveChanges();
			if (this.isAuthenticationSMTP) {
				Transport transport = conn.getTransport("smtp");
				transport.connect(this.smtpHost, this.smtpUser, this.smtpPassword);
				transport.sendMessage(msg, msg.getAllRecipients());
				transport.close();
			} else {
				Transport.send(msg, msg.getAllRecipients());
			}
		} catch (javax.mail.internet.AddressException e) {
			logger.error(e);
			e.printStackTrace();
			throw new FoxBPMException(e.getMessage(), e);
		} catch (javax.mail.MessagingException e) {
			logger.error(e);
			e.printStackTrace();
			throw new FoxBPMException(e.getMessage(), e);
		}
		logger.debug("end send()");
	}

	/**
	 * 异步发送邮件
	 */
	public void asynSend() {
	}

	public boolean isValidEmailAddress(String email) {
		if (StringUtil.isEmpty(email)) {
			return false;
		}
		if (email.indexOf(MAIL_MARK) > 0) {
			return !email.endsWith(MAIL_MARK);
		}
		return false;
	}

	private Address[] getAddress(String[] add) {
		Address[] a = new Address[add.length];
		for (int i = 0; i < add.length; i++) {
			try {
				a[i] = new InternetAddress(add[i]);
			} catch (AddressException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return a;
	}

	public boolean isHtmlModeMail() {
		return this.content_type.equals(MODE_HTML);
	}

	public boolean hasHtmlDesc() {
		if (!this.isHtmlModeMail()) {
			return false;
		}
		return !StringUtil.isEmpty(this.htmlMailDesc);
	}
}