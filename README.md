# Otp Service

## Routes

### POST `/v1/users`

Creates a new user by providing a valid `email`.  
Returns a new `userId` in the body and `Location` header.

#### Request

```json
{
  "email": "example@test.com"
}
```

#### Response

```json
{
  "userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b"
}
```

#### Try it

```shell
curl -d '{"email": "example@test.com"}' \
 -H "Content-Type: application/json" \
 -X POST http://localhost:8080/v1/users
```

### POST `/v1/otp`

Creates a new otp for a given `userId`.  
Returns a new 6 digit `otp` and expiration metadata.

#### Request

```json
{
  "userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b"
}
```

#### Response

```json
{
  "userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b",
  "otp": "988569",
  "expiration": {
    "expiresIn": 30,
    "measurementUnit": "Seconds"
  },
  "creationTime": "2024-08-22T19:42:09.522528Z"
}
```

#### Try it out

```shell
curl -d '{"userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b"}' \
 -H "Content-Type: application/json" \
 -X POST http://localhost:8080/v1/otp
```

### POST `/v1/verifications`

Verify status of `otp` for a given `userId`.  
Returns the otp `status`.

### Request

```json
{
  "userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b",
  "otp": "988569"
}
```

### Response

```json
{
  "userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b",
  "otp": "988569",
  "otpStatus": "ACCEPTED",
  "timestamp": "2024-08-22T19:42:37.077639Z"
}
```

#### Try it out

```shell
curl -d '{"userId": "3419e0d7-ff9b-4a99-a3f4-b567fdff3a2b", "otp": "988569"}' \
 -H "Content-Type: application/json" \
 -X POST http://localhost:8080/v1/verifications
```

## Setup

### Containers

On Mac, with `colima` you need to create a symlink for testcontainers to work

```shell
sudo ln -s $HOME/.colima/docker.sock /var/run/docker.sock
```

### Environment Variables

You need to bind a valid mongoDB connection string to variable `$DB_URI`