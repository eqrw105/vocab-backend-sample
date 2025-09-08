package domain.model.word

// 단어 분류
enum class WordCategoryType {
    /** 시험 관련 단어 묶음(TOEIC 등) */
    Exam,

    /** 단계별 단어 묶음(기본 카테고리) */
    Level,

    /** 사용자 지정 단어 묶음(기본 카테고리) */
    Custom,
}
