package com.javabom.effectivekotlin.chap15

import kotlin.DslMarker

//@HtmlDsl
class Table {
    private var tableRows: List<TableRow> = listOf()

    fun tr(tableRowInitializer: TableRow.() -> Unit) {
        tableRows = tableRows + TableRow().apply(tableRowInitializer)
    }
}

//@HtmlDsl
class TableRow {
    private var tableDatas: List<TableData> = listOf()

    fun td(tableDataInitializer: TableData.() -> Unit) {
        tableDatas + tableDatas + TableData().apply(tableDataInitializer)
    }
}

class TableData {
    var data: String = ""
}

fun table(initializer: Table.() -> Unit): Table {
    return Table().apply(initializer)
}

@DslMarker
annotation class HtmlDsl

fun main() {
    table {
        tr {
            td {
                data = "1번"
            }
            td {
                data = "2번"
            }

//            tr {
//                td {
//                    data = "5번"
//                }
//                td {
//                    data = "6번"
//                }
//            }
        }
        tr {
            td {
                data = "3번"
            }
            td {
                data = "4번"
            }
        }
    }
}
