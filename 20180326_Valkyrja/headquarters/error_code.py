from enum import IntEnum, unique


@unique
class ErrorCode(IntEnum):
    OK = 0
    UnknownState = 1
    NetworkError = 2
    JsonParseError = 3
    InvalidUserNameOrPassword = 4
    UserNotLoggedIn = 5
    CourseNotFound = 6
    TooFewArgument = 7
    StudentNotFound = 8
    ExamNotFound = 9
    ExamAlreadyFinishScoring = 10
    WrongArgumentType = 11
    AnswerSheetNotFound = 12
    SomethingNotFound = 13
    ProblemNotFound = 14
    UserNotFound = 15
    DuplicatedRegistration = 16
    MultipleObjectsReturned = 17
