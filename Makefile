all:
	adb uninstall com.jikuibu.app
	ant debug install
