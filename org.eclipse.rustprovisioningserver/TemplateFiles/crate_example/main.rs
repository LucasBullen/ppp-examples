extern crate time;

fn main() {
    let current_time = time::now();
    println!(
        "Current time is: {}:{}",
        current_time.tm_hour, current_time.tm_min
    );
}

