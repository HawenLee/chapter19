namespace java com.hawen.thrift.pojo

struct UserPojo {
    1:i64 id
    2:string username
    3:string note
}

struct RolePojo {
    1:i64 id
    2:string roleName
    3:string note
}

service UserService {
    UserPojo getUser(1:i64 id)
}

service RoleService {
    list<RolePojo> getRoleByUserId(1: i64 userId)
}