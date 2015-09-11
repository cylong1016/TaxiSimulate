package ui;

import java.awt.Dimension;

import javax.swing.JPanel;

import ui.button.Button;
import ui.frame.Frame;
import ui.textfield.TextField;

/**
 * 测试自己定义的UI组件
 * @author cylong
 * @version 2014年12月29日 下午8:01:00
 */
public class TestUIComp {

	public static void main(String[] args) {
		Frame frame = new Frame();
		Dimension dimen = new Dimension(80, 30);
		JPanel panel = new JPanel();
		frame.add(panel);
		
		// 测试自定义Button
		Button btn = new Button("测试按钮");
		btn.setPreferredSize(dimen);
		panel.add(btn);
		
		// 测试自定义TextField
		TextField textField = new TextField();
		textField.setPreferredSize(dimen);
		panel.add(textField);
		
		frame.setVisible(true);
		frame.start();
	}

}
