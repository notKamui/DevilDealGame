/*
 * The MIT License (MIT)
 *
 * Copyright © 2022 Jimmy "notKamui" Teillard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.notkamui.devilsdeal.engine

class Board private constructor(
    private val width: Int = 5,
    private val height: Int = 5,
    private val tiles: List<Tile>
) {

    init {
        require(width > 0) { "Width must be positive" }
        require(height > 0) { "Height must be positive" }
    }

    /**
     * The columns' data of the board.
     * Each element is a pair,
     * which the first element is the sum of the column's tiles' score,
     * and the second element is the amount of zero tiles in the column.
     */
    val columnsData: List<Pair<Int, Int>> = List(width) { column ->
        var sum = 0
        var zeros = 0
        for (row in 0 until height) {
            val tile = tiles[row * width + column]
            sum += tile.score
            if (tile.score == 0) zeros++
        }
        sum to zeros
    }

    /**
     * The rows' data of the board.
     * Each element is a pair,
     * which the first element is the sum of the row's tiles' score,
     * and the second element is the amount of zero tiles in the row.
     */
    val rowsData: List<Pair<Int, Int>> = List(height) { row ->
        var sum = 0
        var zeros = 0
        for (column in 0 until width) {
            val tile = tiles[row * width + column]
            sum += tile.score
            if (tile.score == 0) zeros++
        }
        sum to zeros
    }

    class Data(
        val twosAmount: Int,
        val threesAmount: Int,
        val zerosAmount: Int,
    ) {
        fun onesAmount(width: Int, height: Int): Int = width * height - twosAmount - threesAmount - zerosAmount
    }

    class Tile(
        val score: Int,
    ) {
        var isRevealed: Boolean = false
        var memoZero: Boolean = false
        var memoOne: Boolean = false
        var memoTwo: Boolean = false
        var memoThree: Boolean = false
    }

    operator fun get(x: Int, y: Int): Tile = tiles[y * width + x]

    companion object {
        fun create(width: Int = 5, height: Int = 5, boardData: Data): Board {
            require(boardData.zerosAmount + boardData.twosAmount + boardData.threesAmount <= width * height) {
                "Too many tiles declared compared to dimensions"
            }
            val tiles = ArrayList<Tile>(width * height)
            repeat(boardData.zerosAmount) { tiles.add(Tile(0)) }
            repeat(boardData.onesAmount(width, height)) { tiles.add(Tile(1)) }
            repeat(boardData.twosAmount) { tiles.add(Tile(2)) }
            repeat(boardData.threesAmount) { tiles.add(Tile(3)) }
            tiles.shuffle()
            return Board(width, height, tiles)
        }

        fun fromTiles(width: Int, height: Int, tiles: List<Tile>): Board = Board(width, height, tiles)
    }
}
