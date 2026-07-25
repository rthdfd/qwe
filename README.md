# 灵创智能体 (CozeLike)

一个类「扣子 (Coze)」风格的安卓 AI 智能体（Bot）客户端，目标系统 **Android 11 (API 30)**。

使用 Kotlin + Material Design 3 + 原生 ViewBinding 实现，通过 GitHub Actions 自动打包成 APK。

## 功能

- **我的智能体**：创建 / 编辑 / 删除自己的 AI Bot，支持名称、头像、简介、提示词 (System Prompt)、模型、温度、最大 Token 等配置。
- **对话**：与任意 Bot 聊天，自动携带 System Prompt 与历史上下文；支持清空对话。
- **发现**：内置多个预设智能体模板（翻译官、文案写手、编程助手等），一键添加到「我的智能体」。
- **设置**：配置 API 地址、API Key、默认模型，保存在本地。

## 接口说明

本应用兼容 **OpenAI 格式的 `/chat/completions` 接口**，因此可对接：

- OpenAI (`https://api.openai.com/v1`)
- Coze 兼容网关 / 任意 OpenAI 兼容代理
- 本地服务：Ollama、vLLM、LM Studio 等（建议同时在设置中开启「明文流量」以支持 http，本应用已默认 `usesCleartextTraffic=true`）

请求体示例：

```json
{
  "model": "gpt-3.5-turbo",
  "messages": [{"role": "system", "content": "..."}, {"role": "user", "content": "..."}],
  "temperature": 0.7,
  "max_tokens": 2048
}
```

## 构建（GitHub Actions 自动打包）

每次推送到 `main` 或 `arena/019f98c1-qwe` 分支，或手动触发 `workflow_dispatch` 时，
GitHub Actions 会执行 `.github/workflows/build.yml`：

1. 安装 JDK 17、Gradle 8.4、Android SDK (platform 33 + build-tools 33.0.2)
2. 执行 `gradle assembleDebug` 生成 Debug APK
3. 上传产物 `cozelike-apk`（包含 `app/build/outputs/apk/debug/app-debug.apk`）

在仓库 **Actions** 页面找到对应任务，下载 APK 即可。

## 本地安装

Debug APK 可直接通过「未知来源」安装到 Android 11 设备。
如需正式发布，请在 `app/build.gradle` 中配置签名后执行 `assembleRelease`。

## 项目结构

```
app/src/main/
  java/com/example/cozelike/
    MyApplication.kt          # 初始化本地数据库
    data/                     # Bot / Message 数据模型与本地存储
    network/                  # OpenAI 兼容聊天客户端
    util/Prefs.kt             # 设置（SharedPreferences）
    ui/                       # 活动与 Fragment
      adapters/               # RecyclerView 适配器
  res/                        # 布局、主题、图标、菜单
```

## 技术栈

- Kotlin 1.9 / Android Gradle Plugin 8.1
- Material Components 1.9 (Material3)
- ViewBinding、RecyclerView、Lifecycle、Coroutines、Gson
- minSdk 26 / targetSdk 30 / compileSdk 33
