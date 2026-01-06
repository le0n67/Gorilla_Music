# GorillaMusic

## 项目介绍
GorillaMusic是一款面向音乐场景的后端服务系统，提供音乐创作（对接AI接口）、音乐信息管理、用户音乐行为操作、支付配置管理等核心能力，支持音乐创作任务调度、音乐信息查询/修改、用户行为记录（点赞等）、系统字典管理等功能。

## 技术栈
- 核心语言：Java
- 框架：Spring/Spring Boot（依赖SpringContext实现Bean获取）
- 持久层：MyBatis（基于Mapper实现数据CRUD）
- 缓存：Redis（用于音乐创作任务缓存）
- 其他：MD5加密（用户密码处理）、枚举类（业务类型定义）等

## 核心功能模块
### 1. 音乐创作（AI对接）
- 对接不同AI音乐创作接口，支持「带歌词音乐」和「纯音乐」两种创作类型的结果查询
- 音乐创作结果包含任务ID、标题、时长、音频链接（普通/高清）、歌词（含时间轴）、创作状态等信息
- 创作任务异步处理：若AI接口未返回结果，将任务缓存至Redis，等待后续重试

### 2. 音乐信息管理
- 音乐信息条件查询、分页查询、数量统计
- 音乐信息推荐类型、用户ID等维度的参数配置与查询
- 支持批量新增/修改、多条件更新/删除音乐信息

### 3. 用户音乐行为
- 用户对音乐的点赞（doGood）操作
- 基于音乐ID+用户ID的行为记录查询、修改、删除
- 行为记录的多条件批量操作

### 4. 系统与支付管理
- 支付配置管理（微信支付API证书路径等）
- 支付码状态管理
- 系统字典的多条件删除、参数校验

## 快速开始
### 环境准备
1. JDK 21
2. MySQL（适配MyBatis数据持久化）
3. Redis（用于任务缓存）
4. Maven（依赖管理）

### 编译与运行
```bash
# 克隆代码（示例）
git clone [项目仓库地址]
cd gorillamusic

# 编译打包
mvn clean package -DskipTests

# 运行项目
java -jar easymusic-common/target/easymusic-common.jar
```

### 核心配置
- 支付相关：`AppConfig` 中配置微信支付API证书路径（`payWechataApiclientKeyPath`）
- 创作接口：通过`apiCode`指定不同AI音乐创作接口实现类，SpringContext动态加载Bean

## 核心代码说明
### 1. 音乐创作结果模型（MusicCreationResultDTO）
封装AI音乐创作的返回结果，包含核心字段：
```java
// 核心字段示例
private String taskId; // 创作任务ID
private String title; // 音乐标题
private Integer duration; // 时长（秒）
private String audioUrl; // 普通音质音频链接
private List<Lyrics> lyricsList; // 歌词列表（含start/end时间轴+文本）
private Boolean createSuccess; // 创作是否成功
```

### 2. AI音乐信息获取逻辑（MusicInfoServiceImpl）
```java
private void getMusicInfoFromAi(MusicTaskDTO musicTaskDto) {
    // 动态获取AI创作接口实现类
    MusicCreateApi musicCreateApi = (MusicCreateApi) SpringContext.getBean(musicTaskDto.getApiCode());
    MusicCreationResultDTO resultDTO = null;
    // 区分音乐/纯音乐查询
    if (MusicTypeEnum.MUSIC.getType().equals(musicTaskDto.getMusicType())) {
        resultDTO = musicCreateApi.musicQuery(musicTaskDto.getTaskId());
    } else {
        resultDTO = musicCreateApi.pureMusicQuery(musicTaskDto.getTaskId());
    }
    // 结果为空则缓存任务，否则处理创作结果
    if (resultDTO == null) {
        redisComponent.addMusicCreateTask(musicTaskDto);
        return;
    }
    musicInfoService.musicCreated(resultDTO);
}
```

### 3. 用户行为操作（MusicInfoActionService）
提供音乐行为的CRUD能力，示例：
```java
// 条件查询用户音乐行为列表
@Override
public List<MusicInfoAction> findListByParam(MusicInfoActionQuery param) {
    return this.musicInfoActionMapper.selectList(param);
}

// 音乐点赞操作
void doGood(String musicId, String userId);
```

## 注意事项
1. AI音乐创作接口需提前配置`apiCode`与实现类的映射关系，确保SpringContext能正确加载Bean；
2. 密码处理采用MD5加密，需保证加密逻辑统一（参考`UserInfoServiceImpl`的密码更新方法）；
3. 缓存的音乐创作任务需配置Redis过期时间，避免任务堆积。
