package de.lars.remotelight.gui;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import de.lars.remotelight.Main;
import de.lars.remotelight.animations.AnimationHandler;
import de.lars.remotelight.utils.LEDScreenColor;

import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class MainGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1822567937316983945L;
	private JPanel contentPane;
	private JPanel panelColorLeft, panelColorRight;
	private JSlider sliderRed, sliderGreen, sliderBlue;
	private boolean screenColorActive;
	private final ButtonGroup buttonGroupAnimation = new ButtonGroup();


	/**
	 * Create the frame.
	 */
	public MainGUI() {
		setResizable(false);
		setTitle("RGB DeskLamp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSetPixelFor = new JLabel("Set pixel for picking the screen color");
		lblSetPixelFor.setFont(new Font("Source Sans Pro", Font.BOLD, 12));
		lblSetPixelFor.setBounds(10, 11, 197, 16);
		contentPane.add(lblSetPixelFor);
		
		JLabel lblLeftwidthheight = new JLabel("Left (width/height):");
		lblLeftwidthheight.setFont(new Font("Source Sans Pro", Font.PLAIN, 11));
		lblLeftwidthheight.setBounds(10, 38, 99, 14);
		contentPane.add(lblLeftwidthheight);
		
		JSpinner spinnerWidthLeft = new JSpinner();
		spinnerWidthLeft.setModel(new SpinnerNumberModel(new Integer(10), null, null, new Integer(1)));
		spinnerWidthLeft.setBounds(119, 35, 41, 20);
		contentPane.add(spinnerWidthLeft);
		
		JSpinner spinnerHeightLeft = new JSpinner();
		spinnerHeightLeft.setModel(new SpinnerNumberModel(new Integer(50), null, null, new Integer(1)));
		spinnerHeightLeft.setBounds(170, 35, 41, 20);
		contentPane.add(spinnerHeightLeft);
		
		JLabel lblRightwidthheight = new JLabel("Right (width/height):");
		lblRightwidthheight.setFont(new Font("Source Sans Pro", Font.PLAIN, 11));
		lblRightwidthheight.setBounds(10, 63, 99, 14);
		contentPane.add(lblRightwidthheight);
		
		JSpinner spinnerWidthRight = new JSpinner();
		spinnerWidthRight.setBounds(119, 60, 41, 20);
		contentPane.add(spinnerWidthRight);
		
		JSpinner spinnerHeightRight = new JSpinner();
		spinnerHeightRight.setBounds(170, 60, 41, 20);
		contentPane.add(spinnerHeightRight);
		
		JLabel lblRgbMixer = new JLabel("RGB Mixer");
		lblRgbMixer.setFont(new Font("Source Sans Pro", Font.BOLD, 12));
		lblRgbMixer.setBounds(10, 88, 68, 16);
		contentPane.add(lblRgbMixer);
		
		JLabel lblRed = new JLabel("Red");
		lblRed.setFont(new Font("Source Sans Pro", Font.PLAIN, 11));
		lblRed.setBounds(10, 115, 46, 14);
		contentPane.add(lblRed);
		
		JLabel lblGreen = new JLabel("Green");
		lblGreen.setFont(new Font("Source Sans Pro", Font.PLAIN, 11));
		lblGreen.setBounds(10, 140, 46, 14);
		contentPane.add(lblGreen);
		
		JLabel lblBlue = new JLabel("Blue");
		lblBlue.setFont(new Font("Source Sans Pro", Font.PLAIN, 11));
		lblBlue.setBounds(10, 165, 46, 14);
		contentPane.add(lblBlue);
		
		sliderRed = new JSlider();
		sliderRed.setValue(0);
		sliderRed.setBounds(42, 113, 200, 26);
		contentPane.add(sliderRed);
		
		sliderGreen = new JSlider();
		sliderGreen.setValue(0);
		sliderGreen.setBounds(42, 138, 200, 26);
		contentPane.add(sliderGreen);
		
		sliderBlue = new JSlider();
		sliderBlue.setValue(0);
		sliderBlue.setBounds(42, 165, 200, 26);
		contentPane.add(sliderBlue);
		
		JButton btnScreenColor = new JButton("Enable ScreenColor");
		btnScreenColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!screenColorActive) {
					screenColorActive = true;
					btnScreenColor.setText("Disable ScreenColor");
					LEDScreenColor.start(Integer.parseInt(spinnerWidthLeft.getValue().toString()), Integer.parseInt(spinnerHeightLeft.getValue().toString()),
														Integer.parseInt(spinnerWidthRight.getValue().toString()), Integer.parseInt(spinnerHeightRight.getValue().toString()));
				} else {
					screenColorActive = false;
					btnScreenColor.setText("Enable ScreenColor");
					LEDScreenColor.stop();
				}
			}
		});
		btnScreenColor.setFont(new Font("Source Sans Pro", Font.PLAIN, 10));
		btnScreenColor.setBounds(10, 219, 125, 23);
		contentPane.add(btnScreenColor);
		
		panelColorLeft = new JPanel();
		panelColorLeft.setBounds(145, 202, 41, 40);
		contentPane.add(panelColorLeft);
		
		panelColorRight = new JPanel();
		panelColorRight.setBounds(201, 202, 41, 40);
		contentPane.add(panelColorRight);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(252, 11, 7, 231);
		contentPane.add(separator);
		
		JLabel lblAnimations = new JLabel("Animation");
		lblAnimations.setFont(new Font("Source Sans Pro", Font.BOLD, 12));
		lblAnimations.setBounds(269, 12, 165, 16);
		contentPane.add(lblAnimations);
		
		JRadioButton rdbtnFlash = new JRadioButton("Flash");
		buttonGroupAnimation.add(rdbtnFlash);
		rdbtnFlash.setBounds(265, 34, 109, 23);
		contentPane.add(rdbtnFlash);
		
		JRadioButton rdbtnJump = new JRadioButton("Jump");
		buttonGroupAnimation.add(rdbtnJump);
		rdbtnJump.setBounds(265, 59, 109, 23);
		contentPane.add(rdbtnJump);
		
		JRadioButton rdbtnRainbow = new JRadioButton("Rainbow");
		buttonGroupAnimation.add(rdbtnRainbow);
		rdbtnRainbow.setBounds(265, 85, 109, 23);
		contentPane.add(rdbtnRainbow);
		
		JRadioButton rdbtnPulse = new JRadioButton("Pulse");
		buttonGroupAnimation.add(rdbtnPulse);
		rdbtnPulse.setBounds(265, 111, 109, 23);
		contentPane.add(rdbtnPulse);
		
		JRadioButton rdbtnBlink = new JRadioButton("Blink");
		buttonGroupAnimation.add(rdbtnBlink);
		rdbtnBlink.setBounds(265, 136, 109, 23);
		contentPane.add(rdbtnBlink);
		
		JSlider sliderSpeed = new JSlider();
		sliderSpeed.setMaximum(250);
		sliderSpeed.setMinimum(10);
		sliderSpeed.setBounds(252, 234, 182, 26);
		contentPane.add(sliderSpeed);
		
		JLabel lblAnimationSpeed = new JLabel("Animation Speed");
		lblAnimationSpeed.setFont(new Font("Source Sans Pro", Font.PLAIN, 11));
		lblAnimationSpeed.setBounds(258, 223, 130, 14);
		contentPane.add(lblAnimationSpeed);
		
		JButton btnStopAnimation = new JButton("Stop Animation");
		btnStopAnimation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationHandler.stop();
			}
		});
		btnStopAnimation.setFont(new Font("Source Sans Pro", Font.PLAIN, 10));
		btnStopAnimation.setBounds(269, 200, 109, 23);
		contentPane.add(btnStopAnimation);
		
		sliderRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Main.setColorSeperateLeft(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
				Main.setColorSeperateRight(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
			}
		});
		sliderGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Main.setColorSeperateLeft(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
				Main.setColorSeperateRight(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
			}
		});
		sliderBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Main.setColorSeperateLeft(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
				Main.setColorSeperateRight(sliderRed.getValue(), sliderGreen.getValue(), sliderBlue.getValue());
			}
		});
		
		rdbtnFlash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationHandler.startAnimation(AnimationHandler.FLASH, sliderSpeed.getValue());
			}
		});
		rdbtnJump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationHandler.startAnimation(AnimationHandler.JUMP, sliderSpeed.getValue());
			}
		});
		rdbtnRainbow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationHandler.startAnimation(AnimationHandler.RAINBOW, sliderSpeed.getValue());
			}
		});
		rdbtnPulse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationHandler.startAnimation(AnimationHandler.PULSE, sliderSpeed.getValue());
			}
		});
		rdbtnBlink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AnimationHandler.startAnimation(AnimationHandler.BLINK, sliderSpeed.getValue());
			}
		});
	}
	
	public void setCurentColorPanel(Color left, Color right) {
		panelColorLeft.setBackground(left);
		panelColorRight.setBackground(right);
	}
}
