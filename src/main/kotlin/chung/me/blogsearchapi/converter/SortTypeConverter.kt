package chung.me.blogsearchapi.converter

import chung.me.blogsearchapi.searchclient.SortType
import org.springframework.core.convert.converter.Converter

class SortTypeConverter : Converter<String, SortType> {
  override fun convert(source: String): SortType? {
    return SortType.of(source)
  }
}
