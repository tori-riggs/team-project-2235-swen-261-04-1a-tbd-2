import { Component, OnInit } from '@angular/core';
import { MessageService } from '../message.service';
import { InboxService } from '../inbox.service'
import { Observable } from 'rxjs';
import { Message } from '../message';

@Component({
  selector: 'app-inbox',
  templateUrl: './inbox.component.html',
  styleUrl: './inbox.component.css'
})

export class InboxComponent {
  username: string = localStorage.getItem("username") ?? "";
  password: string = localStorage.getItem("password") ?? "";
  permissionLevel: string = localStorage.getItem("perms") ?? "";
  messages: Message[] = [];
  selectedMessage?: Message;
  
  check?: Message;

  constructor(private inboxService: InboxService, 
    private messageService: MessageService) 
    {}

  onSelect(message: Message){
    this.selectedMessage = message;
  }

  ngOnInit(): void {
    this.getMessages()

    this.inboxService.newMessageEvent.subscribe(() => {
      this.getMessages()})
  }

  getMessages(){
    this.inboxService.getMessages(this.username,this.password).subscribe(messages => {
      this.messages = messages
    })
  }

  createMessage(userText: string){
    var date = new Date();
    const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    const userMessage = {
      id: 0,
      username: this.username,
      timestamp: days[date.getDay()]+' '+date.getHours().toString()+':'+date.getMinutes().toString(), // Use currentTimeMillis
      text: userText,
    } as Message;
    this.inboxService.createMessage(this.username,this.password,userMessage).subscribe(
      message => {
        this.check = message
        this.inboxService.emitnewMessageEvent()
      })
  }

  deleteMessage(message: Message){
    this.messages = this.messages.filter(m=> m !== message);
    this.inboxService.deleteMessage(this.username,this.password,message.id).subscribe()
  }
  
}
