package infrastructure.table

import domain.model.word.WordCategoryType
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

// 카테고리(시험/레벨/리스트 등) 메타데이터를 저장하는 테이블
object WordCategoriesTable : LongIdTable("word_categories") {
    // 카테고리를 식별하는 고유 슬러그(인덱스/URL용)
    val slug = varchar("slug", 128).uniqueIndex()

    // 카테고리 표시 이름
    val name = varchar("name", 128)

    // 카테고리 유형(enum)
    val type = enumerationByName("type", 32, WordCategoryType::class)

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
}

class WordCategoryDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object Companion : LongEntityClass<WordCategoryDAO>(WordCategoriesTable)

    var slug by WordCategoriesTable.slug
    var name by WordCategoriesTable.name
    var type by WordCategoriesTable.type
    var createdAt by WordCategoriesTable.createdAt
    var updatedAt by WordCategoriesTable.updatedAt
}
