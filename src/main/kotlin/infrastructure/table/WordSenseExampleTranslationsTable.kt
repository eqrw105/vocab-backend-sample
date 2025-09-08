package infrastructure.table
import domain.model.word.Language
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 예문의 언어별 번역을 저장하는 테이블
object WordSenseExampleTranslationsTable : LongIdTable("word_sense_example_translations") {
    // 원문 예문 FK
    val wordSenseExample = reference("word_sense_example_id", WordSenseExamplesTable).index()

    // 번역 언어(enum)
    val language = enumerationByName("language", 16, Language::class).index()

    // 번역된 텍스트
    val text = text("text")

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class SenseExampleTranslationDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object Companion : LongEntityClass<SenseExampleTranslationDAO>(WordSenseExampleTranslationsTable)

    var example by WordSenseExampleDAO referencedOn WordSenseExampleTranslationsTable.wordSenseExample
    var language by WordSenseExampleTranslationsTable.language
    var text by WordSenseExampleTranslationsTable.text
    var createdAt by WordSenseExampleTranslationsTable.createdAt
    var updatedAt by WordSenseExampleTranslationsTable.updatedAt
}
