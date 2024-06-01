# Ktor-Notes-Server

Backend kotlin server for [Android-Notes-Client](https://github.com/carrot2803/android-notes-client.git).  
The server is built using the Ktor framework which connects to a postgresql database

## App Availability

You can test server functionalities via postman
at [Collection](https://www.postman.com/maintenance-saganist-72934879/workspace/notes/collection/26820239-a2a0273a-4f10-4331-840d-d8117e901da0)

## Installation

<details>
<summary><code>There are several ways you can run this application</code></summary>

- [Downloading repository as ZIP](https://github.com/carrot2803/ktor-notes-server/archive/refs/heads/master.zip)
- Running the following command in a terminal, assuming you have [GitHub CLI](https://cli.github.com/) installed:

```sh
git clone https://github.com/carrot2803/ktor-notes-server.git
```

</details>

## Routes

### Auth Routes

1. <u>/register</u> [POST]: Registers a new user.
2. <u>/login</u> [POST]: Logs in the user.

### Notes Routes

1. <u>/notes</u> [GET]: Returns a list of all notes for an authorized user
2. <u>/notes</u> [POST]: Adds and stores a users new note.
3. <u>/notes</u> [PUT]: Updates the contents of a user's note
4. <u>/notes/&lt;id&gt;</u> [DELETE]: Deletes a specified note.

# Package Structure

    src                   # Root Package
    .
    ├── models            # Contains data classes representing various models in the app.
    |
    ├── repository        # Serves as an interface between application and database.
    |   
    ├── routes            # Handles routing and directs incoming requests to appropriate handlers.
    |
    └── service           # Provides authentication services and managing user authentication.
