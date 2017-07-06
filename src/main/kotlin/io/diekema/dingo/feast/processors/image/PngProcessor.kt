package io.diekema.dingo.feast.processors.image

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.diekema.dingo.feast.Asset
import io.diekema.dingo.feast.Exchange
import io.diekema.dingo.feast.processors.Processor
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileWriter
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Created by rdiekema-idexx on 7/6/17.
 */
class PngProcessor(val outputDir: String?) : Processor {
    val JSON = jacksonObjectMapper()

    override fun process(exchange: Exchange) {
        val sprites = mutableListOf<Sprite>()
        var sumX = 0
        var maxHeight = 0
        val spriteMap = mutableMapOf<String, Map<String, Any>>()

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
            val css = """.${sprite.name} { background: url('sprites.png') ${xOff}px 0px; height: ${sprite.height}px; width: ${sprite.width}px; }"""
            spriteMap.put(sprite.name, mapOf(
                    Pair("x", xPos),
                    Pair("y", sprite.height),
                    Pair("url", "sprites.png"),
                    Pair("css", css)
                )
            )
            xOff -= sprite.width
            cssString += css
        }

        Paths.get(outputDir).toAbsolutePath().toFile().mkdirs()
        val absoluteOutputPath = Paths.get(outputDir + File.separator + "sprites.png").toAbsolutePath()
        val spriteMapPath = Paths.get(outputDir + File.separator + "sprites.json").toAbsolutePath()
        val cssPath = Paths.get(outputDir + File.separator + "sprites.css").toAbsolutePath()
        File(absoluteOutputPath.toString()).createNewFile()
        File(cssPath.toString()).createNewFile()
        
        JSON.writeValue(FileWriter(spriteMapPath.toFile(), false), spriteMap)
        val cssFileWriter = FileWriter(cssPath.toFile(), false)
        cssFileWriter.write(cssString)
        cssFileWriter.flush()
        cssFileWriter.close()

        ImageIO.write(bufferSpriteSheet, "PNG", absoluteOutputPath.toFile())
        exchange.assets = mutableListOf(Asset("sprites.png", "", absoluteOutputPath.toString(), "png"))
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