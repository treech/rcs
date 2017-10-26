
# 开发人员须知：
1、AS需安装插件Alibaba Java Coding Guidelines与CmdSupport;
2、提前约定好各组件资源文件命名，每个资源文件的名称必须以组件名开始(否组组件之间资源名可能重名);
3、公共的资源和代码统一放在common包下(包括ButterKnife、glide、okhttp等开源库);
4、因打包而成的aar中不包含依赖的远程库，各个特性组件所依赖的库版本必须与common库一致，
    以防止第三方库重复打包造成的冲突；(也即所有开源库均放在common库中，各个特性组件各取所需，
    除非确认此依赖包仅自己使用才能单独放在特性组件里);
5、在公共组件common库中每添加一个开源库请在home基座的proguard-rules.pro文件中写好混淆规则;

# 编译须知：
1、目前主工程rcs有message、contacts、phone、me、oa五个特性组件以及
    公共组件common、home基座(修改common公共库、home基座时请慎重);
2、编译特性组件时请先清空build文件以免R文件id重复导致app无法运行(清空build文件请用ClearAllBuildFiles.bat脚本,
    清空完以后再点击锤子进行全局构建);
3、例如修改完message组件，请选择Tasks->build->build进行编译，编译完执行再Tasks->other->copyAPK即可
    copy message组件的meesage.apk至home基座的assets目录(其它特性组件同理);
4、修改完common组件，请选择Tasks->build->assembleRelease进行编译，编译完执行CopyAAR.bat脚本;