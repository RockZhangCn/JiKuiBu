all:
	ant clean
	adb uninstall com.jikuibu.app
	ant debug install

clean:
	ant clean

uninstall:
	adb uninstall com.jikuibu.app

install:
	ant installd
