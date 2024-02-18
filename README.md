### meteor-client

# What is

The original Meteor Client was conflicting with the Future Client. I investigated the issue and realized that the cause of the conflict was not very important and could be removed. I removed the conflicting mixins and modules, changed the ClickGUI key, and altered the Prefix character. The client 1.20.1 is compatible with Fabric.

# What's changed

`-` PlayerListHudMixin mixin was removed due to a conflict with future and the BetterTab module was deleted.

`+` The default prefix has been changed to "+".

`+` The default clickgui key has been changed to 'P'.
