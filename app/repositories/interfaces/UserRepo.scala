package repositories.interfaces

import domain.user.User

trait UserRepo extends BaseRepo[User, User.id]