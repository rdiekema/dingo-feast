package io.diekema.dingo.feast.processors.image

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.Processor
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Created by rdiekema-idexx on 7/6/17.
 */
class PngProcessor(val outputDir: String, val subdir: String, val filename: String) : Processor {
    val JSON = jacksonObjectMapper()
    val IMAGE_FORMAT = "PNG"

    override fun process(exchange: Exchange) {
        val sprites = mutableListOf<Sprite>()
        var sumX = 0
        var maxHeight = 0
        val spriteMap = mutableMapOf<String, Map<String, Any>>()

        val imageFilename = subdir + File.separator + "$filename.png"

        exchange.assets.forEach { asset ->
            yieldSprite(asset)?.let { sprite ->
                sprites.add(sprite)
                sumX += sprite.width
                maxHeight = maxOf(maxHeight, sprite.height)
            }
        }

        sprites.sortDescending()

        val bufferSpriteSheet = BufferedImage(sumX, maxHeight, BufferedImage.TYPE_INT_ARGB)
        var xPos = 0
        var xOff = 0
        val g = bufferSpriteSheet.graphics
        var cssString = ""
        sprites.forEach { sprite ->
            g.drawImage(sprite.image, xPos, 0, null)
            xPos += sprite.width
            val css = """.${sprite.name} { background: url('$imageFilename') ${xOff}px 0px; height: ${sprite.height}px; width: ${sprite.width}px; }"""
                spriteMap.put(sprite.name, mapOf(
                        Pair("x", xPos),
                        Pair("y", sprite.height),
                        Pair("url", imageFilename),
                        Pair("css", css)
                )
            )
            xOff -= sprite.width
            cssString += css
        }

        Paths.get(outputDir + File.separator + imageFilename).toAbsolutePath().toFile().mkdirs()
        val absoluteImageOutputPath = Paths.get(outputDir + File.separator + imageFilename).toAbsolutePath()
        File(absoluteImageOutputPath.toString()).createNewFile()

        ImageIO.write(bufferSpriteSheet, IMAGE_FORMAT, absoluteImageOutputPath.toFile())
        exchange.assets = mutableListOf(Asset(null, cssString, null, "css"), Asset(null, JSON.writeValueAsString(spriteMap), null, "json"))
    }

    fun yieldSprite(asset: Asset): Sprite? {
        asset.originalPath?.let {
            val image = ImageIO.read((asset.originalPath!!.toFile()))
            return Sprite(asset.name!!, image.width, image.height, image)
        }

        return null
    }
}


data class Sprite(val name: String, val width: Int, val height: Int, val image: BufferedImage) : Comparable<Sprite> {
    override fun compareTo(other: Sprite): Int {
        return this.height.compareTo(other.height)
    }
}