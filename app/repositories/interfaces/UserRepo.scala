package repositories.interfaces

import domain.user.User

trait UserRepo extends BaseRepo[User, User.ForCreate, User.ModelId]