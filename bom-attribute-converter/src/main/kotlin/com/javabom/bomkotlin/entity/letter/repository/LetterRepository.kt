package com.javabom.bomkotlin.entity.letter.repository

import com.javabom.bomkotlin.entity.letter.Letter
import org.springframework.data.jpa.repository.JpaRepository

interface LetterRepository : JpaRepository<Letter, Long>
