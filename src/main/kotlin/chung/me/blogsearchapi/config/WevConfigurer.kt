package chung.me.blogsearchapi.config

import chung.me.blogsearchapi.converter.SortTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WevConfigurer : WebMvcConfigurer {
  override fun addFormatters(registry: FormatterRegistry) {
    registry.addConverter(SortTypeConverter())
  }
}
