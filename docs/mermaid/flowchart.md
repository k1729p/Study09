```mermaid
flowchart LR
 subgraph SpringBoot Application
  CTL[Controller]:::orangeBox
  DST[Dataset]:::greenBox
 end
 subgraph API Clients
  WBR[Web\nBrowser]
  CURL[Curl]
 end

 WBR & CURL <--> CTL <--> DST
 
 classDef greenBox   fill:#00ff00,stroke:#000,stroke-width:3px
 classDef orangeBox  fill:#ffa500,stroke:#000,stroke-width:3px
```