# soft_body_simulation :panda_face: :soccer:
#### In my opinion - one of the coolest simulations!

## What can you say about it? :raising_hand:
_**I have only two words for you: INCREDIBLY COOL!**_

I've always wanted to create some kind of simulation related to physics.  
And finally, **I did it**!  

_P.S. The work I have done is not perfect.  
I didn't go deep into formulas.  
I didn't try to create a comprehensive universal soft body engine.  
Not at all.  
I am driven only by wild curiosity, what are you talking about? :D_

## How it works? :hatching_chick:
> Everything written below is just a **crooked interpretation** of the coolest articles on **soft body physics**.  
> Want two-page formulas and buzzwords? Google it! :3
> 
> _Who seeks will find._  
> _Whoever goes will come._  
> _Those who google will not notice the answer in the first link._

#### #1 ~ «Introduction»
Well, let's understand how **soft body simulation** works.  

First, we need to understand **what the body itself** is. And by the way, it won't take long to find the answer (that's why I'm here lol):
- Particles

Yes, **particles** are everywhere.  
It is from these little freaks that everything around us consists: the phone that you hold in your hands, and the hands themselves.  

And, you know, if you want to create a **soft body**, you don't have to **beat around the bush** - **steal the idea from nature**!

#### #2 ~ «Particles»
_So, okay, we have particles. But what exactly are they?_

Here is the basic set of properties for each **particle**:
- **location**
- **velocity**

I mean, it's pretty primitive.  
We have a particle location **(x, y)** and a **velocity vector**.  
It remains for each frame to update the location, adding velocity to it.  

_It's all? Do we have a soft body?_

**Lol, no.. :D**

#### #3 ~ «Springs»
Having only **particles**, you can get, for example, a **gas**, but obviously not a **soft body**, because, as you know, a body is a body, because it is an **ordered set of interconnected particles** (_crystal lattice_).  

And, if we were making a regular **rigid body**, we wouldn't even have to tie anything.  
But here's a **soft body**..  
It should behave like **jelly**, or any other **soft** object, that is, **deform, and then recover**.  

And the **«Spring Mass System»** will help us to achieve this effect.  

I will not retell [smart definitions from Wikipedia](https://en.wikipedia.org/wiki/Soft-body_dynamics), but I will explain in simple words:  

Our task is to **split the particles into pairs**. Each pair forms a **spring**.  
Next, we need to go through all the **springs** (_pairs of two particles_) and, based on some data, calculate the **next position of the particles** in space.  

Thus, the **particles become bound to each other**.  
And the **system of spring masses** allows you to achieve the effect of **jelly**, since when the particles move away from each other, they are forced to move towards each other (**spring compression**), otherwise, if the particles are too close to each other (closer than a certain distance / **state of rest**), they forced to move in opposite directions (**stretching the spring**).  

Summarizing the above, we have:
- **Particles**
- **Springs (a pair of two particles)**

This is actually enough to create a **soft body**.  

#### #4 ~ «Soft body»
Well, let's briefly go through the main steps:
1. Create **particles**.
2. Connect them with **springs**.
3. Iterate through the springs every frame and apply all **sorts of tricky formulas** to achieve the spring effect (move away the particles - they return to each other; bring the particles closer - they push each other with all their might; stretch the spring and release it - it will contract and stretch, making **harmonic oscillations**, until it reaches a **state of rest**).

Apart from the **spring mass system**, you will probably need a **gravity simulation** (just add a constant to the y-axis of the velocity vector every frame), a **collision resolution algorithm** (because you don't want your soft body to fall forever) and **a lot of curiosity** (no way without it)!  

## Demo 🔥
<kbd>
  <a href="https://www.youtube.com/watch?v=r1O-JMuGJYU">
    <img src="https://user-images.githubusercontent.com/56264511/175388973-2723ae31-da4b-422f-9a5f-dac133e92698.png">
  </a>
</kbd>

## Thanks! Gonkee :sparkling_heart: (u r cutie btw)
* #### [But How DO Soft Body Simulations Work?](https://www.youtube.com/watch?v=kyQP4t_wOGI&t=527s&ab_channel=Gonkee) by Gonkee
