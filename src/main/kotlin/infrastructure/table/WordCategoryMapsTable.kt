package infrastructure.table

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 카테고리와 단어 간 N:N 매핑 및 가중치를 저장하는 테이블
object WordCategoryMapsTable : LongIdTable("word_category_maps") {
    // 연결된 카테고리 FK
    val wordCategory = reference("word_category_id", WordCategoriesTable).index()

    // 연결된 단어 FK
    val word = reference("word_id", WordsTable).index()

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    init {
        uniqueIndex(wordCategory, word)
    }
}

class WordCategoryMapDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object Companion : LongEntityClass<WordCategoryMapDAO>(WordCategoryMapsTable)

    var category by WordCategoryDAO referencedOn WordCategoryMapsTable.wordCategory
    var word by WordDAO referencedOn WordCategoryMapsTable.word
    var createdAt by WordCategoryMapsTable.createdAt
    var updatedAt by WordCategoryMapsTable.updatedAt
}
