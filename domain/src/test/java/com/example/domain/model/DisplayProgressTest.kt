package com.example.domain.model

import com.google.common.truth.Truth.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

 internal class DisplayProgressTest: Spek({
    describe("DisplayProgress"){
        describe("progress is 0"){
            it("returns NOT_STARTED"){
                assertThat(DisplayProgress.fromProgress(0)).isEqualTo(DisplayProgress.NOT_STARTED)
            }
        }
        describe("progress is 1"){
            it("returns IN_PROGRESS"){
                assertThat(DisplayProgress.fromProgress(1)).isEqualTo(DisplayProgress.IN_PROGRESS)
            }
        }
        describe("progress is 99"){
            it("returns IN_PROGRESS"){
                assertThat(DisplayProgress.fromProgress(99)).isEqualTo(DisplayProgress.IN_PROGRESS)
            }
        }
        describe("progress is 100"){
            it("returns MASTERED"){
                assertThat(DisplayProgress.fromProgress(100)).isEqualTo(DisplayProgress.MASTERED)
            }
        }
        describe("progress is minus 1"){
            it("returns NOT_STARTED"){
                assertThat(DisplayProgress.fromProgress(-1)).isEqualTo(DisplayProgress.NOT_STARTED)
            }
        }
        describe("progress is 101"){
            it("returns NOT_STARTED"){
                assertThat(DisplayProgress.fromProgress(101)).isEqualTo(DisplayProgress.NOT_STARTED)
            }
        }
    }

})