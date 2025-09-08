package infrastructure.table

import domain.model.word.Language
import domain.model.word.PartOfSpeech
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 단어 원형(레마) 메타데이터
object WordsTable : LongIdTable("words") {
    // 표제어 텍스트
    val text = varchar("text", 128).index()

    // 언어
    val language = enumerationByName("language", 16, Language::class).index()

    // 품사(없을 수 있음)
    val partOfSpeech = enumerationByName("part_of_speech", 32, PartOfSpeech::class).nullable()

    // 생성/수정 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    init {
        // 동일 언어 내 동일 표기+품사 중복 방지(품사가 null인 경우도 1개만 허용)
        uniqueIndex(text, language, partOfSpeech)
    }
}

class WordDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<WordDAO>(WordsTable)

    var text by WordsTable.text
    var language by WordsTable.language
    var partOfSpeech by WordsTable.partOfSpeech
    var createdAt by WordsTable.createdAt
    var updatedAt by WordsTable.updatedAt
}
