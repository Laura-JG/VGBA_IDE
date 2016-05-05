import java.io.File;

public interface Options {
	public static final int OPTION_NAME = 0;
	public static final int OPTION_PATH = 1;
	public static final int OPTION_TEXT_SIZE = 2;
	public static final int OPTION_ARCH = 3;

	public static final int TYPE_FIRST_RUN = 10;
	public static final int TYPE_NEW_PROJECT = 11;
	public static final int TYPE_PROJECT_SPECIFIC = 12;

	public static final String OPTION_DEFAULT_ARCH = "-marm";
	public static final String OPTION_DEFAULT_PATH = System.getProperty("user.home") + File.separator + "VGBA_WS";
	public static final int OPTION_DEFAULT_TEXT_SIZE = 13;
	public static final String OPTION_DEFAULT_NAME = "Project_";
	public static final String OPTION_DEFAULT_PSETTINGS_FILE_NAME = ".settings.cfg";
}
