/**
 * Program #2.
 * This program is a text adventure game that responds to
 * player (text) input. Players can type directions to
 * move to different rooms, can pick up and drop items, 
 * and have an inventory of items that they can look at.
 * 
 * The program also randomly connects the rooms, so that
 * each time the program's run the world is configured randomly.
 * 
 * There's an issue with the useObject function in Player. The only
 * way to not have the program throw a null pointer exception when you
 * try to use anything is to place the "You can't use that!" as an else
 * statement inside the loop. This means that for every item you have
 * in your inventory that isn't a train ticket, you'll get the
 * "You can't use that!" response, right up until the loop hits the
 * index with the train ticket (if the player has one).
 * 
 * Also, we know that trainTicketsViewable breaks the style guidelines
 * because it's a class variable and isn't final, but we couldn't figure
 * out any other way to check if the object the player's trying to use
 * (in useObject) is the train ticket.
 * 
 * Authors: Shayne Clementi (snclemen@ucsc.edu)
 * and Yunyi Ding (yding13@ucsc.edu)
 */

Welcome to Amusement Park Adventure.
After a long day having fun at Amusement Park, you're ready to leave.
Find a train ticket and ride the train back home.

Type north, south, east, or west to move to different areas.
Type location to see what room you're in.
Type look to see if there's any objects in the area.
Type 'pick up' and 'drop [object name]' to pick up and drop objects.
Type inventory to see what you're holding.
Type use to use an object. Not every object can be used.

location

You are in the Entrance Plaza
look

You see some coins.
pick up

You picked up 50 coins.
inventory

You are holding: coins
south

You are in the Entrance Plaza
esat

What?
east

You are in the Roller Coaster
look

You see some train ticket.
pick up

You picked up 1 train ticket.
south

You are in the Merry-Go-Round
look

You see some beautiful squirrels.
south

You are in the Corsair
look

You see some candy.
pick up

You picked up 2 candy.
inventory

You are holding: coins
You are holding: train ticket
You are holding: candy
drop candy

You dropped candy.
inventory

You are holding: coins
You are holding: train ticket
look

You see some candy.
east

You are in the Merry-Go-Round
south

You are in the Corsair
north

You are in the Entrance Plaza
west

You are in the Gift Shop
look

You see some secrets.
south

You are in the Ferris Wheel
west

You are in the Ferris Wheel
north

You are in the Train Stop
inventory

You are holding: coins
You are holding: train ticket
use train ticket

You can't use that.
You get on the train and leave Amusement Park.