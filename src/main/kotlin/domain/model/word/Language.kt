package domain.model.word

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 지원 언어
@Serializable
enum class Language {
    /** 영어 */
    @SerialName("en")
    En,

    /** 한국어 */
    @SerialName("ko")
    Ko,
}
