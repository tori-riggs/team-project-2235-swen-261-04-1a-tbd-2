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
  
  check: string = '';

  constructor(private inboxService: InboxService, 
    private messageService: MessageService) 
    {}

  onSelect(message: Message){
    this.selectedMessage = message;
  }

  ngOnInit(): void {
    console.log("test")
    this.getMessages()
    console.log("test")
  }

  getMessages(){
    this.inboxService.getMessageForUser(this.username,this.password).subscribe(messages => this.messages = messages)
  }

  createMessage(text: string){
    this.inboxService.createMessage(this.username,this.password,text).subscribe(
      message => {
        this.check = message.text
        console.log(`${this.check}`)
      })
  }

  deleteMessage(id: number){
    this.inboxService.deleteMessage(this.username,this.password,id)
  }



  
}
