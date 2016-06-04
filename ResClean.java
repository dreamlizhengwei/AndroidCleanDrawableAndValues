package com.example.basicapplication.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 清理无用资源
 */
public class ResClean {

	public static void main(String[] args) {
		// cleanRes("D:\\Downloads\\Android\\adt\\sdk\\tools\\lint.bat",
		// "D:\\Downloads\\Android\\XiaoMaWorkPlace\\XMGJ_USEV3.11");
		deleValues("D:\\Android\\adt\\sdk\\tools\\lint.bat",
				"工程目录（ProjectDir）");
	}

	/**
	 * 清除无用的values资源
	 * 
	 * @param lintPath
	 *            lint.bat路径,一般位于sdk目录下tools/lint.bat
	 * @param projectPath
	 *            工程路径
	 */
	public static void deleValues(String lintPath, String projectPath) {
		String values = "values";
		String cmd = lintPath + " --check UnusedResources " + projectPath;
		Process process = null;
		BufferedReader bf = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(cmd);
			InputStream in = process.getInputStream();
			bf = new BufferedReader(new InputStreamReader(in));
			// 临时文件名
			String tempFileName = "temp";
			String line;
			System.out.println("开始：");
			while (true) {
				line = bf.readLine();
				if (line == null) {
					break;
				}
				System.out.println(line);
				if (line.contains(values)) {
					String arr[] = line.split(":");
					// 文件相对路径
					String filePath = new String(arr[0]);
					// 要删除的行号
					int deleLine = Integer.parseInt(arr[1]);
					File alterFile = new File(projectPath + File.separator
							+ filePath);
					// 临时文件
					File tempFile = new File(alterFile.getParent()
							+ File.separator + tempFileName
							+ alterFile.getName());
					BufferedReader alterBr = new BufferedReader(
							new InputStreamReader(
									new FileInputStream(alterFile), "utf-8"));
					FileOutputStream fos = new FileOutputStream(tempFile);
					// 当前行数
					int lineNum = 0;
					String alterLine;
					// 逐行读取文件，弃掉要删除的行
					while (true) {
						alterLine = alterBr.readLine();
						if (alterLine == null) {
							break;
						}
						lineNum++;
						if (lineNum != deleLine) {
							fos.write((alterLine + "\r\n").getBytes("utf-8"));
						} else {
							fos.write("\r\n".getBytes());
						}
					}
					fos.close();
					alterBr.close();
					// 删除旧的
					alterFile.delete();
					// 重命名新的
					tempFile.renameTo(alterFile);
					System.out.println("结束。");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (process != null) {
				process.destroy();
			}
		}
	}

	/**
	 * 清除无用的图片和布局资源
	 * 
	 * @param lintPath
	 *            lint.bat路径,一般位于sdk目录下tools/lint.bat
	 * @param projectPath
	 *            工程路径
	 */
	public static void cleanRes(String lintPath, String projectPath) {
		String drawable = "drawable";
		String layout = "layout";
		String cmd = lintPath + " --check UnusedResources " + projectPath;
		Process process = null;
		BufferedReader bf = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(cmd);
			InputStream in = process.getInputStream();
			bf = new BufferedReader(new InputStreamReader(in));
			int count = 0;
			String line;
			System.out.println("删除资源文件文件：");
			while (true) {
				line = bf.readLine();
				if (line == null) {
					break;
				}
				// 包含drawable或者layout说明是图片或者布局
				if (line.contains(drawable) || line.contains(layout)
						&& !line.contains("dimen")) { // 后面的dimen放置字符串资源中命名可能带layout或者drawable字符
					System.out.println("路径---> " + line);
					line = line.substring(0, line.indexOf(":"));
					String path = projectPath + File.separator + line;
					File f = new File(path);
					if (f.exists()) {
						count++;
						System.out.println(f.getAbsolutePath());
						f.delete();
					}
				}
			}
			System.out.println("删除结束。");
			System.out.println("删除文件数：" + count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (process != null) {
				process.destroy();
			}
		}

	}

}
