from enum import IntEnum, unique


@unique
class UserState(IntEnum):
    Login = 0
    Logout = 1
    InExam = 2
    Idle = 3
