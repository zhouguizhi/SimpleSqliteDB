# SimpleSqliteDB
手写orm数据库,手动创建表,数据库创建位置可以自定义,支持多用户登录,分库功能,用于见demo.
1:DBField注解用于在实体bean的字段,用于表示创建表的字段.
2：DBNotField表示改bean中某个字段不想加入到创建表中.
3:DBNotNullField表示表中的字段值不能为空.
4:DBPrivateKeyField表示注解.
5:DBTable用在bean上表示表明.
6:DBNotNullAndUnique表示值不为空且唯一.

