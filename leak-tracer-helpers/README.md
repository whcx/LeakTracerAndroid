基于LeakTracer的内存泄漏检测工具：
LeakTracer文档 ：
http://www.andreasen.org/LeakTracer/
https://github.com/fredericgermain/LeakTracer


目前是检测LibGwEngine.so库中的内存泄漏。

1，开启Native内存泄漏检测：
	GritPlayerActivity.java
		mEnableLeakTracer = true;
	GWCPEngine\Render\GWEngine\CMakeLists.txt
		MEM_LEAK_TRACER = ON

2,  内存泄漏检测相关源码：
		GWCPEngine\external\memLeakTracer\src\cpp
	内存泄漏分析工具：
		GWCPEngine\external\memLeakTracer\LeakTracerHelpers
		这里需要用到addr2line，本地先要添加到环境变量。
		
3， 运行应用一段时间，退出应用，将保存内存分配跟踪快照到/sdcard/GWRenderEngine/files/memTracer/native_heap.txt。
	退出应用，可以通过执行命令:
	adb shell am start -n "com.huawei.android.launcher/.unihome.UniHomeLauncher" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
	这个命令会跳转到Launcher，从而让前台app退到后台。

4，内存泄漏分析：
	拷贝 库 GWCPEngine\Render\GWEngine\build-android\build\intermediates\cmake\debug\obj\arm64-v8a\libGWEngine.so 
	到GWCPEngine\external\memLeakTracer\LeakTracerHelpers目录下。
	拷贝 跟踪文件 /sdcard/GWRenderEngine/files/memTracer/native_heap.txt GWCPEngine\external\memLeakTracer\LeakTracerHelpers目录下。
	（执行脚本GWCPEngine\scripts\PullLog.bat 会导出/sdcard/GWRenderEngine/files/memTracer/native_heap.txt文件到GWCPEngine\scripts\memTracer目录下。）

	执行命令：./leak-analyze-addr2line64 libGWEngine.so native_heap.txt >info.txt，解析疑似内存泄漏的调用堆栈。
	

附录，其他native内存检测工具。
	1.malloc debug:
	adb shell setprop wrap.com.gritworld.gritmobile '"LIBC_DEBUG_MALLOC_OPTIONS=backtrace\ leak_track\ logwrapper"'
	adb shell am force-stop com.gritworld.gritmobile
    关掉应用，再打开
	adb shell am dumpheap -n PID /data/local/tmp/heap.txt
	开启后，apk运行太慢。
	
    2.malloc hook:
	adb shell setprop wrap.com.gritworld.gritmobile '"LIBC_HOOKS_ENABLE=1"'
	adb shell setprop wrap.com.gritworld.gritmobile '"LIBC_DEBUG_MALLOC_OPTIONS=backtrace logwrapper"'
	adb shell setprop wrap.com.gritworld.gritmobile '"LIBC_HOOKS_ENABLE=1 leak_track logwrapper"'
	adb shell am force-stop com.gritworld.gritmobile
	adb shell am dumpheap -n PID /data/local/tmp/heap.txt
	开启后，apk运行太慢。
	
	3.HeapSnapshot:
	https://github.com/albuer/heapsnap
	需要把开源库中的源码编译出可执行文件，库。编译时，开源库中缺少以下依赖库：libbase.so，libc_malloc_debug.so，libc++.so，libdexfile_support.so，libdl.so，liblzma.so，libunwindstack.so
	可以从手机导出，或者下载AndroidAOSP源码自己编译出相关库。
	使用编译出的文件，在Demo中没能检测到native内存泄漏，后续再看看。
	
	4.Address Sanitizer:
	安装时提示，手机设备不支持。
	
	
	
	
