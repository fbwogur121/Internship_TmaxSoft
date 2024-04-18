package com.Tmax.refactorCodeGpt.dto.request
import com.Tmax.refactorCodeGpt.dto.Message
data class ChatGptRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
) {
    companion object {
        fun of(fileExtension: String, code: String): ChatGptRequest =
            ChatGptRequest(messages = listOf(makePrompt(fileExtension, code)))
        private fun makePrompt(fileExtension: String, code: String): Message =
            Message(role = "code refactoring teacher", content = """This code's file extension: $fileExtension
                $code
            """.trimIndent())
    }
}

//package com.Tmax.refactorCodeGpt.dto.request
//
//import com.Tmax.refactorCodeGpt.dto.Message
//
//data class ChatGptRequest(
//    val model: String = "gpt-3.5-turbo",
//    val messages: List<Message>,
//) {
//    companion object {
//        fun of(fileExtension: String, code: String): ChatGptRequest =
//            ChatGptRequest(messages = listOf(makePrompt(fileExtension, code)))
//
//        private fun makePrompt(fileExtension: String, code: String): Message =
//            Message(role = "user", content = """
//                You role is perfect code refactoring prompt.
//                Refactoring has the effect of improving the readability of the code and reducing its complexity, and these benefits are derived from a simpler, cleaner, or more expressive internal architecture or object model to improve the maintainability of the source code and improve scalability. allows you to create
//                The following conditions are examples of seven refactoring methods and conditions.
//                1. There is no need to separate variable declaration and initial value assignment.
//                Before:
//                String ssId = "";
//                long amt = 0L;
//
//                ssId = svcDto.getSsId();
//                amt = svcDto.getAmt().longValue();
//                After:
//                String ssId = svcDto.getSsId();
//                long amt = svcDto.getAmt().longValue();
//
//                2. Replace simple if-else statements with the ternary operator.
//                Before:
//                if(svcDto.getSsId() != null)
//                {
//                    calcDtl.setRealSsId(svcDto.getRealSsId());
//                }
//                else
//                {
//                    calcDtl.setRealSsId(svcDto.getSsId());
//                }
//
//                After:
//                calcDtl.setRealSsId(svcDto.getSsId() != null ? svcDto.getRealSsId() : calcDtl.setRealSsId(svcDto.getSsId());
//
//                3. Convert the if-else statement into a switch statement.
//                Before:
//                if(DataDef.CD_100.equals(refDto.getDcCd()))
//                {
//                    refDto.setAmt(100);
//                    break;
//                }
//
//                if(DataDef.CD_200.equals(refDto.getDcCd()))
//                {
//                    refDto.setAmt(100);
//                    break;
//                }
//
//                if(DataDef.CD_300.equals(refDto.getDcCd()))
//                {
//                    refDto.setAmt(100);
//                    break;
//                }
//
//                After:
//                switch(refDto.getDcCd()) {
//                    case DataDef.CD_100:
//                        refDto.setAmt(100);
//                        break;
//                    case DataDef.CD_200:
//                        refDto.setAmt(100);
//                        break;
//                    case DataDef.CD_300:
//                        refDto.setAmt(100);
//                        break;
//                    default:
//                        // handle the case where refDto.getDcCd() doesn't match any of the cases
//                }
//
//                4. Break down long methods into smaller, more manageable methods.
//                Before:
//                public void processOrder(Order order) {
//                  // Do some validation
//                  if (!order.isValid()) {
//                    throw new IllegalArgumentException("Invalid order");
//                  }
//
//                  // Do some processing
//                  if (order.isProcessed()) {
//                    // Do some more processing
//                    if (order.isPaid()) {
//                      // Update the database
//                      Database.updateOrder(order);
//                    } else {
//                      throw new IllegalArgumentException("Order not paid");
//                    }
//                  } else {
//                    throw new IllegalArgumentException("Order not processed");
//                  }
//                }
//
//                After:
//                public void processOrder(Order order) {
//                  validateOrder(order);
//                  processOrderDetails(order);
//                  Database.updateOrder(order);
//                }
//
//                private void validateOrder(Order order) {
//                  if (!order.isValid()) {
//                    throw new IllegalArgumentException("Invalid order");
//                  }
//                }
//
//                private void processOrderDetails(Order order) {
//                  if (!order.isProcessed()) {
//                    throw new IllegalArgumentException("Order not processed");
//                  }
//
//                  if (!order.isPaid()) {
//                    throw new IllegalArgumentException("Order not paid");
//                  }
//
//                  // Do some more processing
//                }
//
//                5. Remove duplicate code and separate methods.
//                Before:
//                public void updateCustomer(Customer customer) {
//                  if (customer.getStatus().equals("Active")) {
//                    // Do some processing
//                    sendNotification(customer.getEmail());
//                    Database.updateCustomer(customer);
//                  } else if (customer.getStatus().equals("Suspended")) {
//                    // Do some processing
//                    sendNotification(customer.getEmail());
//                    Database.updateCustomer(customer);
//                  } else if (customer.getStatus().equals("Inactive")) {
//                    // Do some processing
//                    sendNotification(customer.getEmail());
//                    Database.updateCustomer(customer);
//                  }
//                }
//
//                public void deleteCustomer(Customer customer) {
//                  if (customer.getStatus().equals("Active")) {
//                    // Do some processing
//                    sendNotification(customer.getEmail());
//                    Database.deleteCustomer(customer);
//                  } else if (customer.getStatus().equals("Suspended")) {
//                    // Do some processing
//                    sendNotification(customer.getEmail());
//                    Database.deleteCustomer(customer);
//                  } else if (customer.getStatus().equals("Inactive")) {
//                    // Do some processing
//                    sendNotification(customer.getEmail());
//                    Database.deleteCustomer(customer);
//                  }
//                }
//
//                After:
//                public void updateCustomer(Customer customer) {
//                  processCustomer(customer);
//                  Database.updateCustomer(customer);
//                }
//
//                public void deleteCustomer(Customer customer) {
//                  processCustomer(customer);
//                  Database.deleteCustomer(customer);
//                }
//
//                private void processCustomer(Customer customer) {
//                  // Do some processing
//                  sendNotification(customer.getEmail());
//                }
//
//                6. Use design patterns.
//                Before:
//                public class MySingleton {
//                  private static MySingleton instance;
//
//                  private MySingleton() {
//                    // Do some initialization
//                  }
//
//                  public static MySingleton getInstance() {
//                    if (instance == null) {
//                      instance = new MySingleton();
//                    }
//
//                    return instance;
//                  }
//                }
//
//                After:
//                public class MySingleton {
//                  private static class SingletonHolder {
//                    private static final MySingleton INSTANCE = new MySingleton();
//                  }
//
//                  private MySingleton() {
//                    // Do some initialization
//
//                7. Algorithm and data structure optimization
//                Before:
//                public List<Integer> findPrimeNumbers(int limit) {
//                  List<Integer> primeNumbers = new ArrayList<>();
//
//                  for (int i = 2; i <= limit; i++) {
//                    boolean isPrime = true;
//                    for (int j = 2; j < i; j++) {
//                      if (i % j == 0) {
//                        isPrime = false;
//                        break;
//                      }
//                    }
//
//                    if (isPrime) {
//                      primeNumbers.add(i);
//                    }
//                  }
//
//                  return primeNumbers;
//                }
//
//                After:
//                public List<Integer> findPrimeNumbers(int limit) {
//                  List<Integer> primeNumbers = new ArrayList<>();
//                  boolean[] isPrime = new boolean[limit + 1];
//                  Arrays.fill(isPrime, true);
//
//                  for (int i = 2; i * i <= limit; i++) {
//                    if (isPrime[i]) {
//                      for (int j = i * i; j <= limit; j += i) {
//                        isPrime[j] = false;
//                      }
//                    }
//                  }
//
//                  for (int i = 2; i <= limit; i++) {
//                    if (isPrime[i]) {
//                      primeNumbers.add(i);
//                    }
//                  }
//
//                  return primeNumbers;
//                }
//
//                Refactor the following code for better readability and maintainability.
//                However, you must restructure the code without changing the results.
//                Don't say ANY explain. Just response the code strictly.
//                This code's file extension: $fileExtension
//                Here is the code:
//                ```
//                $code
//                ```
//
//                Respond start with the line 'Code:'
//            """.trimIndent())
//    }
//}
