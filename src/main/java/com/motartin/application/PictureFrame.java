package com.motartin.application;

import com.motartin.connector.Connector;
import com.motartin.connector.DefaultConnector;
import com.motartin.connector.SMBConnector;
import com.motartin.filehandling.PictureProvider;
import com.motartin.filehandling.RandomPicProvider;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import static com.motartin.application.Constants.App.APPLICATION_NAME;
import static com.motartin.application.Constants.App.IMAGE_REFRESH_TIMEOUT_DEFAULT_IN_SECONDS;
import static com.motartin.application.Constants.App.STANDARD_PROPERTY_FILE_NAME;
import static com.motartin.application.Constants.PropertyKey.CONNECTOR_TYPE;
import static com.motartin.application.Constants.PropertyKey.IMAGE_REFRESH_TIMEOUT;
import static com.motartin.utils.ImageScaler.scaleToFittingImage;
import static com.motartin.utils.PropertyReader.readConfig;

/**
 * An application that displays images from a configured source on a JFrame.
 */
@Slf4j
public class PictureFrame extends JFrame {

	public static final String ICON_PNG = "icon.png";
	private static final String PLACEHOLDER_PNG = "placeholder.png";
	
	private JLabel imageLabel;
	private PictureProvider picProvider;
	private final BufferedImage placeholder = ImageIO.read(Objects.requireNonNull(PictureFrame.class.getResourceAsStream("/" + PLACEHOLDER_PNG)));

	public static void main(String[] args) throws IOException {
		new PictureFrame();
	}

	public PictureFrame() throws IOException {
		super(APPLICATION_NAME);

		initProperties();

		createFrame();

		final String connectorType = System.getProperty(CONNECTOR_TYPE);

		CompletableFuture.runAsync(() -> {
			this.picProvider = new RandomPicProvider(getConnectorForType(connectorType));
			log.debug("Got PictureProvider {}", picProvider.getClass());
		});

		int refreshRate = Integer.parseInt(
				System.getProperty(IMAGE_REFRESH_TIMEOUT, IMAGE_REFRESH_TIMEOUT_DEFAULT_IN_SECONDS));
		log.debug("Starting timer with refresh rate of {} seconds", refreshRate);
		Timer imageRefreshTimer = new Timer();
		imageRefreshTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				placeNextImage();
				redraw();
			}
		}, 1000L, refreshRate * 1000L);
	}

	private static void initProperties() {
		final Properties properties = readConfig(STANDARD_PROPERTY_FILE_NAME);
		if (!properties.containsKey(CONNECTOR_TYPE) || properties.getProperty(CONNECTOR_TYPE).isBlank()) {
			properties.setProperty(CONNECTOR_TYPE, "default");
		}

		System.getProperties().putAll(properties);
	}

	private static Connector getConnectorForType(String connectorType) {
		return switch (connectorType) {
			case "smb" -> new SMBConnector();
			default -> new DefaultConnector();
		};
	}

	private void placeNextImage() {
		if (picProvider == null) {
			log.debug("picProvider is null");
			return;
		}
		Image nextFromProvider = picProvider.getNextImage();
		BufferedImage nextPicture = nextFromProvider == null ? placeholder : toBufferedImage(nextFromProvider);
		imageLabel.setIcon(new ImageIcon(scaleToFittingImage(getWidth(), getHeight(), nextPicture)));
	}

	private void createFrame() throws IOException {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER || e.getKeyChar() == KeyEvent.VK_ESCAPE) {
					dispose();
					System.exit(0);
				}
			}
		});
		setIconImage(ImageIO.read(Objects.requireNonNull(PictureFrame.class.getResourceAsStream("/" + ICON_PNG))));

		imageLabel = new JLabel("", SwingConstants.CENTER);
		placeNextImage();
		add(imageLabel);
		validate();
		pack();
		setVisible(true);
	}
	
	private void redraw() {
		invalidate();
		validate();
		update(getGraphics());
	}

	public static String getAppLocationPath() {
		String path = PictureFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		return new File(path).getParentFile().getAbsolutePath();
	}

	static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}
