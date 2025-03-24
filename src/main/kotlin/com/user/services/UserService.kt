package com.user.services

import com.user.dtos.UserDto
import com.user.mapper.toUserEntity
import com.user.models.UserModel
import com.user.producer.UserProducer
import com.user.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userProducer: UserProducer) {

    fun createUser(userDto: UserDto): UserModel {
        val newUser = userDto.toUserEntity(userDto)
        userProducer.publishMessageEmail(newUser)
        return userRepository.save(newUser)
    }

    fun getAllUsers(): List<UserModel> { return userRepository.findAll() }

    @Transactional
    fun deleteUser(document: String) {
        val userToDelete = userRepository.findUserByDocument(document) ?: throw Exception("User not found.")
        userRepository.deleteById(userToDelete.id)
    }
}