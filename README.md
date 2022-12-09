# FinalProjectEkt
This time for real doe...

Contains bare-boned version of server and client.
Current capabilities:

- Server creates a ServerSocket on port 5555.
- Server connects to MySQL database (root username, locally stored)
- Server listens for clients
- Server can disconnect and reconnect "seamlessly"
- Server can be safely closed using the exit button or the X button (with an asterisk* -> open connections to client not yet handled properly)

- Client can connect to a server over port 5555 and an address (user inserted, defaults to localhost)
- Client can close the program using the "exit" button (X button is not properly mapped yet)
- Client loads an (empty) login page for the Ekt system after connecting to server

TODO:
Plenty, TBD.
