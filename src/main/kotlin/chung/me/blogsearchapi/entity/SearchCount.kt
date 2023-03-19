package chung.me.blogsearchapi.entity

import jakarta.persistence.*

@Entity
@Table(name = "search_count", uniqueConstraints = [UniqueConstraint(name = "search_count_uk", columnNames = ["query"])])
class SearchCount(
  query: String,
) {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long = 0L
    private set

  @Column(length = 1024, nullable = false)
  var query: String = query
    private set

  @Column(nullable = false)
  var count: Long = 0L
    private set

  fun increaseCount() {
    count += 1
  }
}
