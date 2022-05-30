# 使用教程
## 状态机配置
利用spring ioc容器配置state machine

### 使用Enable注解
应用中存在下列两个注解会启动状态机
  
- @EnableStateMachine
- @EnableStateMachineFactory

### 状态机配置属性
继承下列两个类并覆盖其方法可以快速地配置状态机的属性。

- EnumStateMachineConfigurerAdapter（状态与事件类型为枚举，继承于StateMachineConfigurerAdapter）
- StateMachineConfigurerAdapter

可配置Api：

    /**
	 * Callback for {@link StateMachineModelConfigurer}.
	 *
	 * @param model the {@link StateMachineModelConfigurer}
	 * @throws Exception if configuration error happens
	 */
	void configure(StateMachineModelConfigurer<S, E> model) throws Exception;

	/**
	 * Callback for {@link StateMachineConfigurationConfigurer}.
	 *
	 * @param config the {@link StateMachineConfigurationConfigurer}
	 * @throws Exception if configuration error happens
	 */
	void configure(StateMachineConfigurationConfigurer<S, E> config) throws Exception;

	/**
	 * Callback for {@link StateMachineStateConfigurer}.
	 *
	 * @param states the {@link StateMachineStateConfigurer}
	 * @throws Exception if configuration error happens
	 */
	void configure(StateMachineStateConfigurer<S, E> states) throws Exception;

	/**
	 * Callback for {@link StateMachineTransitionConfigurer}.
	 *
	 * @param transitions the {@link StateMachineTransitionConfigurer}
	 * @throws Exception if configuration error happens
	 */
	void configure(StateMachineTransitionConfigurer<S, E> transitions) throws Exception;


### 配置状态机状态
覆盖 `void configure(StateMachineStateConfigurer<S, E> states) throws Exception` 进行状态属性的配置

#### 一级状态机
1、StateMachineConfigurerAdapter
	
	@Configuration
	@EnableStateMachine
	public class Config1Strings
	        extends StateMachineConfigurerAdapter<String, String> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<String, String> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial("S1")
	                .end("SF")
	                .states(new HashSet<String>(Arrays.asList("S1","S2","S3","S4")));
	    }
	
	}
2、EnumStateMachineConfigurerAdapter

    @Configuration
	@EnableStateMachine
	public class Config1Enums
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States, Events> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial(States.S1)
	                .end(States.SF)
	                .states(EnumSet.allOf(States.class));
	    }

	}



#### 多级状态机（存在嵌套状态）

    @Configuration
	@EnableStateMachine
	public class Config2
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States, Events> states)
	            throws Exception {
	        states
	            .withStates()                   
	                .initial(States.S1)
	                .state(States.S1)
	                .and()
	                .withStates()
	                    .parent(States.S1)   //配置子状态
	                    .initial(States.S2)
	                    .state(States.S2);
	    }
	
	}


### 配置状态转移
覆盖 `void configure(StateMachineTransitionConfigurer<S, E> transitions) throws Exception` 进行状态转移的配置。
 
支持三种不同类型的转换，包括外部，内部和本地。转换可以由一个外部发送的信号或者定时器信号触发的，或者自动触发。   

- **external**      外部转换，有状态转移  
- **internal**      内部转换，无状态转移
- **local**         本地转换
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	            throws Exception {
	        transitions
	            .withExternal()                               
	                .source(States.S1).target(States.S2)
	                .event(Events.E1)
	                .and()
	            .withInternal()
	                .source(States.S2)
	                .event(Events.E2)
	                .and()
	            .withLocal()
	                .source(States.S2).target(States.S3)
	                .event(Events.E3);
	    }


	
	
### 配置guard安全机制
 状态保护机制，对一个状态转移进行评估，评估值为true允许状态转移，评估值为false禁止转移。
    
    @Configuration
	@EnableStateMachine
	public class Config4
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	            throws Exception {
	        transitions
	            .withExternal()
	                .source(States.S1).target(States.S2)
	                .event(Events.E1)
	                .guard(guard())   // guard
	                .and()
	            .withExternal()
	                .source(States.S2).target(States.S3)
	                .event(Events.E2)
	                .guardExpression("true");
	
	    }
	
	    @Bean
	    public Guard<States, Events> guard() {
	        return new Guard<States, Events>() {
	
	            @Override
	            public boolean evaluate(StateContext<States, Events> context) {
	                return true;
	            }
	        };
	    }
	
	}


### 配置action动作
- action动作可以是伴随transition状态转移进行的。
    
	
	    @Configuration
		@EnableStateMachine
		public class Config51
		        extends EnumStateMachineConfigurerAdapter<States, Events> {
		
		    @Override
		    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
		            throws Exception {
		        transitions
		            .withExternal()
		                .source(States.S1)
		                .target(States.S2)
		                .event(Events.E1)
		                .action(action());   //
		    }
		
		    @Bean
		    public Action<States, Events> action() {
		        return new Action<States, Events>() {
		
		            @Override
		            public void execute(StateContext<States, Events> context) {
		                // do something
		            }
		        };
		    }
		
		}

- action动作也可以与状态的进出相关     

	    @Configuration
		@EnableStateMachine
		public class Config52
		        extends EnumStateMachineConfigurerAdapter<States, Events> {
		
		    @Override
		    public void configure(StateMachineStateConfigurer<States, Events> states)
		            throws Exception {
		        states
		            .withStates()
		                .initial(States.S1, action())  
		                .state(States.S1, action(), null)
		                .state(States.S2, null, action())
		                .state(States.S2, action())
		                .state(States.S3, action(), action());
		    }
		    @Bean
		    public Action<States, Events> action() {
		        return new Action<States, Events>() {
		
		            @Override
		            public void execute(StateContext<States, Events> context) {
		                // do something
		            }
		        };
		    }
		  }



在上面的例子，有以下几点：   

- 定义了初始状态S1的动作。
- 定义了状态S1的进入动作为action()，退出动作为null。
- 定义了状态S2的进入动作为null，退出动作为action()。
- 定义了状态S2的状态动作为action()。
- 定义了状态S3的进入动作为action(),退出动作为action().
- 注意:在状态机启动时，分别执行两次action(),一次是初始化S1时执行的，一次是进入S1状态时执行的。

更多灵活的配置请查看api

#### 下列的一个例子说明这些动作的执行顺序:   
状态机配置：


    @Configuration
	public class StateMachine {
	
	    private final static Log log = LogFactory.getLog(StateMachine.class);
	
	    @Configuration
	    @EnableStateMachine
	    static class StateMachineConfig
	            extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	        @Override
	        public void configure(StateMachineStateConfigurer<States, Events> states)
	                throws Exception {
	            states
	                    .withStates()
	                    .initial(States.S1, initAction(States.S1))
	                    .state(States.S1, enAction(States.S1))
	                    .state(States.S2, enAction(States.S2), exAction(States.S2))
	                    .state(States.S2, stateAction(States.S2));
	        }
	
	        @Override
	        public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	                throws Exception {
	            transitions
	                    .withExternal()
	                    .source(States.S1)
	                    .target(States.S2)
	                    .event(Events.E1)
	                    .guard(guard())
	                    .and()
	                    .withExternal()
	                    .source(States.S2)
	                    .target(States.S1)
	                    .event(Events.E2);
	        }
	
	
	        private Guard<States, Events> guard() {
	            return new Guard<States, Events>() {
	                @Override
	                public boolean evaluate(StateContext context) {
	                    return true;
	                }
	            };
	        }
	
	        private Action<States, Events> initAction(final States state) {
	            return new Action<States, Events>() {
	                @Override
	                public void execute(StateContext<States, Events> context) {
	                    System.out.println("initAction triggerd by State:" + state);
	                }
	            };
	        }
	
	        private Action<States, Events> stateAction(final States state) {
	            return new Action<States, Events>() {
	                @Override
	                public void execute(StateContext<States, Events> context) {
	                    System.out.println("stateAction triggerd by State:" + state);
	                }
	            };
	        }
	
	
	        private Action<States, Events> enAction(final States state) {
	            return new Action<States, Events>() {
	                @Override
	                public void execute(StateContext<States, Events> context) {
	                    System.out.println("enAction triggerd by State:" + state);
	                }
	            };
	        }
	
	        private Action<States, Events> exAction(final States state) {
	            return new Action<States, Events>() {
	                @Override
	                public void execute(StateContext<States, Events> context) {
	                    System.out.println("exAction triggerd by State:" + state);
	                }
	            };
	        }
	
	    }
	
	    public enum States {
	        S1,
	        S2
	    }
	
	    public enum Events {
	        E1,
	        E2
	    }
	
	}
	
信号输入&action动作执行：

    Welcome to Spring Shell. For assistance press or type "hint" then hit ENTER.
	spring> sm start
	initAction triggerd by State:S1
	enAction triggerd by State:S1
	State machine started
	spring> sm even E1
	enAction triggerd by State:S2
	stateAction triggerd by State:S2
	Event E1 send
	spring> sm event E2
	exAction triggerd by State:S2
	Event E2 send
	enAction triggerd by State:S1
	spring>


#### state action
- 与输入退出action不同，state actions是进入状态后退出状态之前开始执行的，所以在发生状态退出的时候，state actions还没有完成，则可以取消执行。

- state actions是通过spring标准的`TaskScheduler `以及`Runnable `执行的，我们可以通过`ScheduledFuture`取消任务。


我们可以通过以下配置设置状态退出时取消未完成的state actions：  
**注：**这是官方的配置方法，经过测试没有效果，待研究。

	@Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config
            .withConfiguration()
                .stateDoActionPolicy(StateDoActionPolicy.IMMEDIATE_CANCEL);
    }
设置状态转移等待state actions执行，设置等待时间。等待时间到期后进行状态转移但不会取消state actions的执行：

    @Override
	public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
	    config
	        .withConfiguration()
	            .stateDoActionPolicy(StateDoActionPolicy.TIMEOUT_CANCEL)
	            .stateDoActionPolicyTimeout(10, TimeUnit.SECONDS);
	}


#### action异常处理器
##### Transition Action Error Handling
配置转移动作异常处理器。
	
    @Configuration
	@EnableStateMachine
	public class Config53
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	            throws Exception {
	        transitions
	            .withExternal()
	                .source(States.S1)
	                .target(States.S2)
	                .event(Events.E1)
	                .action(action(), errorAction());
	    }
	    @Bean
	    public Action<States, Events> action() {
	        return new Action<States, Events>() {
	            @Override
	            public void execute(StateContext<States, Events> context) {
	                throw new RuntimeException("MyError");
	            }
	        };
	    }
	    @Bean
	    public Action<States, Events> errorAction() {
	        return new Action<States, Events>() {
	            @Override
	            public void execute(StateContext<States, Events> context) {
	                // RuntimeException("MyError") added to context
	                Exception exception = context.getException();
	                exception.getMessage();
	            }
	        };
	    }
	
	}

或者：

    @Override
	public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	        throws Exception {
	    transitions
	        .withExternal()
	            .source(States.S1)
	            .target(States.S2)
	            .event(Events.E1)
	            .action(Actions.errorCallingAction(action(), errorAction()));
	}

##### State Action Error Handling
类似错误处理也可以用在entry action、exit action以及state action中。  
使用`stateEntry`、`stateDo `、`stateExit `定义errorActions：
    
	@Configuration
	@EnableStateMachine
	public class Config55
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States, Events> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial(States.S1)
	                .stateEntry(States.S2, action(), errorAction())
	                .stateDo(States.S2, action(), errorAction())
	                .stateExit(States.S2, action(), errorAction())
	                .state(States.S3);
	    }
	
	    @Bean
	    public Action<States, Events> action() {
	        return new Action<States, Events>() {
	
	            @Override
	            public void execute(StateContext<States, Events> context) {
	                throw new RuntimeException("MyError");
	            }
	        };
	    }
	
	    @Bean
	    public Action<States, Events> errorAction() {   //异常处理
	        return new Action<States, Events>() {
	
	            @Override
	            public void execute(StateContext<States, Events> context) {
	                // RuntimeException("MyError") added to context
	                Exception exception = context.getException();
	                exception.getMessage();
	            }
	        };
	    }
	}



### 配置伪状态
伪状态配置通常通过配置状态和转换得到。伪状态会自动添加到状态机中作为状态。

#### 初始状态
使用initial()配置初始状态

 	@Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
            .withStates()
                .initial(States.S1, initialAction())   //设置初始状态
                .end(States.SF)
                .states(EnumSet.allOf(States.class));
    }


#### 终止状态
使用end()配置终态

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
            .withStates()
                .initial(States.S1)
                .end(States.SF)
                .states(EnumSet.allOf(States.class));
    }

#### 历史状态
状态历史（history）的概念是由David Harel在最初的状态图形式模型中引入的。它被用来保存一个Region在上次退出时的state configuration。Region可以恢复它上一次退出时的状态配置。例如:当该Region重新Active(从处理一个中断返回),或者有一个返回到它的历史的内部转变。（This is achieved simply by terminating a Transition on the desired type of history Pseudostate inside the Region）。该功能的好处是它消除了用户显式地跟踪历史的需要，这可以显著的简化状态机模型。  
提供了两种历史伪装态： 
 

- 浅历史（shallowHistory）   
  代表只返回到最近一次的状态配置的最顶层子状态，使用缺省入口规则进入   

- 深度历史（deepHistory）   
  代表了对区域最近一次访问的完整状态配置。一个Transition如果连接到deepHistory的话，代表着一个Transition连接在所保存的state configuration的最深层的State上，包括执行沿途遇到的所有Entry Behavior

使用：

	        @Override
	        public void configure(StateMachineStateConfigurer<States, Events> states)
	                throws Exception {
	            states
	                    .withStates()
	                    .initial(States.S1)
	                    .state(States.S2)
	                    .and()
	                        .withStates()
	                        .parent(States.S2)
	                        .initial(States.S2I)
	                        .state(States.S21, action(States.S21))
	                        .state(States.S22, action(States.S22))
	                        .history(States.S2H, StateConfigurer.History.SHALLOW).
	                        and()
	                            .withStates()
	                            .parent(States.S22)
	                            .initial(States.S22I)
	                            .state(States.S221)
	                            .state(States.S222);
	        }
	
	        @Override
	        public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	                throws Exception {
	            transitions
	                    .withHistory()
	                        .source(States.S2H)       // 如果没有历史状态，则默认调到S22状态
	                        .target(States.S22)
	                    .and()
	                    .withExternal()
	                    .source(States.S1)
	                    .target(States.S2)
	                    .event(Events.E2)
	                    .and()
	                    .withExternal()
	                    .source(States.S2)
	                    .target(States.S1)
	                    .event(Events.E1)
	                    .and()
	                    .withExternal()
	                    .source(States.S2)
	                    .target(States.S21)
	                    .event(Events.E21)
	                    .and()
	                    .withExternal()
	                    .source(States.S21)
	                    .target(States.S22)
	                    .event(Events.E22)
	                    .and()
	                    .withExternal()
	                    .source(States.S2)
	                    .target(States.S2H)
	                    .event(Events.E2H)
	                    .and()
	                    .withExternal()
	                    .source(States.S22)
	                    .target(States.S221)
	                    .event(Events.E221);
	        }
	

**分析：**  
上述代码构建的状态机简略图如下：


![历史状态](/pic/historyState.png)

当从状态S222退出到S1，历史状态保存S2H的是S22。   

- 历史模式是SHALLOW，连接到S2H，当前状态是S2、S22、S221
- 历史模式是DEEP，连接到S2H，当前状态是S2、S22、S222




#### 状态选择(choice State)
状态选择需要在状态之间转换中(transitions)定义且发挥作用的。在特定的源状态发生转换时，可以设置guard进行评估以选择目标状态，再进行状态转换。使用的withChoice()方法进行配置，这是一个first/then/last结构，相当于if/else if/else。注意:这里必须指定last，以便条件都不满足时进行默认转换。

	@Configuration
	@EnableStateMachine
	public class Config13
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States, Events> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial(States.SI)
	                .choice(States.S1)
	                .end(States.SF)
	                .states(EnumSet.allOf(States.class));
	    }
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	            throws Exception {
	        transitions
	            .withChoice()
	                .source(States.S1)
	                .first(States.S2, s2Guard())
	                .then(States.S3, s3Guard())
	                .last(States.S4);
	    }
	
	    @Bean
	    public Guard<States, Events> s2Guard() {
	        return new Guard<States, Events>() {
	
	            @Override
	            public boolean evaluate(StateContext<States, Events> context) {
	                return false;
	            }
	        };
	    }
	
	    @Bean
	    public Guard<States, Events> s3Guard() {
	        return new Guard<States, Events>() {
	
	            @Override
	            public boolean evaluate(StateContext<States, Events> context) {
	                return true;
	            }
	        };
	    }
	
	}


#### 交叉点状态(Junction State)
Junction State与Choice State的区别在学术上的，它们都可以用if/else if/else实现其功能。所以在代码功能上与choice state几乎完全一样，都是允许一个状态输入，绑定多个状态输出，在进行评估后选择其一进行输出。

	@Configuration
	@EnableStateMachine
	public class Config20
	        extends EnumStateMachineConfigurerAdapter<States, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States, Events> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial(States.SI)
	                .junction(States.S1)
	                .end(States.SF)
	                .states(EnumSet.allOf(States.class));
	    }
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
	            throws Exception {
	        transitions
	            .withJunction()
	                .source(States.S1)
	                .first(States.S2, s2Guard())
	                .then(States.S3, s3Guard())
	                .last(States.S4);
	    }
	
	    @Bean
	    public Guard<States, Events> s2Guard() {
	        return new Guard<States, Events>() {
	
	            @Override
	            public boolean evaluate(StateContext<States, Events> context) {
	                return false;
	            }
	        };
	    }
	
	    @Bean
	    public Guard<States, Events> s3Guard() {
	        return new Guard<States, Events>() {
	
	            @Override
	            public boolean evaluate(StateContext<States, Events> context) {
	                return true;
	            }
	        };
	    }
	
	}

#### 状态分支(Fork State)
状态分支选择需要在状态之间转换中(transitions)定义且发挥作用的，可以显式进入一个或多个状态。  
![fork state](https://docs.spring.io/spring-statemachine/docs/2.0.0.RELEASE/reference/htmlsingle/images/statechart7.png)
	
	@Configuration
	@EnableStateMachine
	public class Config14
	        extends EnumStateMachineConfigurerAdapter<States2, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States2, Events> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial(States2.S1)
	                .fork(States2.S2)
	                .state(States2.S3)
	                .and()
	                .withStates()
	                    .parent(States2.S3)
	                    .initial(States2.S2I)
	                    .state(States2.S21)
	                    .state(States2.S22)
	                    .end(States2.S2F)
	                    .and()
	                .withStates()
	                    .parent(States2.S3)
	                    .initial(States2.S3I)
	                    .state(States2.S31)
	                    .state(States2.S32)
	                    .end(States2.S3F);
	    }
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States2, Events> transitions)
	            throws Exception {
	        transitions
	            .withFork()
	                .source(States2.S2)
	                .target(States2.S22)
	                .target(States2.S32);
	    }
	
	}



#### 状态合并(Join State)
将一个或多个源状态连接到一个目标状态中，当所以源状态都激活时，则会自动合并成目标状态。

![join state](https://docs.spring.io/spring-statemachine/docs/2.0.0.RELEASE/reference/htmlsingle/images/statechart8.png)

	@Configuration
	@EnableStateMachine
	public class Config15
	        extends EnumStateMachineConfigurerAdapter<States2, Events> {
	
	    @Override
	    public void configure(StateMachineStateConfigurer<States2, Events> states)
	            throws Exception {
	        states
	            .withStates()
	                .initial(States2.S1)
	                .state(States2.S3)
	                .join(States2.S4)
	                .state(States2.S5)
	                .and()
	                .withStates()
	                    .parent(States2.S3)
	                    .initial(States2.S2I)
	                    .state(States2.S21)
	                    .state(States2.S22)
	                    .end(States2.S2F)
	                    .and()
	                .withStates()
	                    .parent(States2.S3)
	                    .initial(States2.S3I)
	                    .state(States2.S31)
	                    .state(States2.S32)
	                    .end(States2.S3F);
	    }
	
	    @Override
	    public void configure(StateMachineTransitionConfigurer<States2, Events> transitions)
	            throws Exception {
	        transitions
	            .withJoin()
	                .source(States2.S2F)
	                .source(States2.S3F)
	                .target(States2.S4)
	                .and()
	            .withExternal()
	                .source(States2.S4)
	                .target(States2.S5);
	    }
	}


#### 入口/退出状态
在状态区域中指定进入时的状态以及退出时的状态。

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {
        states
        .withStates()
            .initial("S1")
            .state("S2")
            .state("S3")
            .and()
            .withStates()
                .parent("S2")
                .initial("S21")
                .entry("S2ENTRY")
                .exit("S2EXIT")
                .state("S22");
    }