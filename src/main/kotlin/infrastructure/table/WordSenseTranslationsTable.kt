package infrastructure.table

import domain.model.word.Language
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 의미의 언어별 번역(글로스/설명)을 저장하는 테이블
object WordSenseTranslationsTable : LongIdTable("word_sense_translations") {
    // 대상 의미 FK
    val WordSense = reference("word_sense_id", WordSensesTable).index()

    // 번역 언어(enum)
    val language = enumerationByName("language", 16, Language::class).index()

    // 글로스/번역어
    val gloss = varchar("gloss", 512)

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    init {
        index(false, WordSense, language)
    }
}

class WordSenseTranslationDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<WordSenseTranslationDAO>(WordSenseTranslationsTable)

    var sense by WordSenseDAO referencedOn WordSenseTranslationsTable.WordSense
    var language by WordSenseTranslationsTable.language
    var gloss by WordSenseTranslationsTable.gloss
    var createdAt by WordSenseTranslationsTable.createdAt
    var updatedAt by WordSenseTranslationsTable.updatedAt
}
