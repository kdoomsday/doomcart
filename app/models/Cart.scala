package models

/**
  * User: Eduardo Barrientos
  * Date: 19/09/16
  * Time: 06:43 PM
  */
case class Cart(
  owner: User,
  items: List[StoreItem]
)
