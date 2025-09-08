package infrastructure.table

import domain.model.word.PartOfSpeech
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 단어의 의미를 저장하는 테이블
object WordSensesTable : LongIdTable("word_senses") {
    // 대상 단어 FK
    val word = reference("word_id", WordsTable).index()

    // 품사(enum)
    val pos = enumerationByName("pos", 32, PartOfSpeech::class).index()

    // 영어 정의(옵션)
    val defEn = text("def_en").nullable()

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class WordSenseDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<WordSenseDAO>(WordSensesTable)

    var word by WordDAO referencedOn WordSensesTable.word
    var pos by WordSensesTable.pos
    var defEn by WordSensesTable.defEn
    var createdAt by WordSensesTable.createdAt
    var updatedAt by WordSensesTable.updatedAt
}
