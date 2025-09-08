package infrastructure.table

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 단어 의미별 예문을 저장하는 테이블
object WordSenseExamplesTable : LongIdTable("word_sense_examples") {
    // 연결된 의미(WordSense) FK
    val wordSense = reference("word_sense_id", WordSensesTable).index()

    // 예문 텍스트
    val sentence = text("sentence")

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class WordSenseExampleDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object Companion : LongEntityClass<WordSenseExampleDAO>(WordSenseExamplesTable)

    var sense by WordSenseDAO referencedOn WordSenseExamplesTable.wordSense
    var sentence by WordSenseExamplesTable.sentence
    var createdAt by WordSenseExamplesTable.createdAt
    var updatedAt by WordSenseExamplesTable.updatedAt
}
