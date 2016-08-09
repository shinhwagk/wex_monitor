# wex_monitor
         
| manage | | master | |agent|
| --- | ---|--- | --- | --- |
| | | get:/api/agent/heart | ~>|  |
| | |get:/api/agent/monitor/{task_name} | ~><br><~ | 404 <br>200 |
| get:/api/master/nodes|~> | |  |  |
| get:/api/master/monitor/{task_name}|~> | |  |  |

