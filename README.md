# Spring Boot 初始化项目

## 功能概述

本项目实现了以下功能：

- 用户登录注册
- 权限校验
- AOP（面向切面编程）
- 日志记录

## 使用说明

- 配置后数据库账号密码后直接运行（提前建好数据库，无需建表会自动初始化数据库表）

### 控制器注解

- **@LoginRequired**：表示该接口需要用户登录后才能访问。

  ```java
  @GetMapping("/protected")
  @LoginRequired
  public String protectedEndpoint() {
      return "This is a protected endpoint.";
  }

- **@RoleRequired(value = User.Role.ADMIN)**：表示该接口需要管理员权限才能访问。

  ```java
  @GetMapping("/admin")
  @RoleRequired(value = User.Role.ADMIN)
  public String adminEndpoint() {
      return "This is an admin-only endpoint.";
  }

- 创建新表文件路径：src/main/resources/db/migration/V1__Initial_schema.sql[清空数据库（如果有旧数据）。运行自动建表脚本。]
